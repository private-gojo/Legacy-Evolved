package tunnel

import (
	"encoding/binary"
	"errors"
	"fmt"
	"io"
	"net"
	"strconv"
	"time"
)

const (
	socksVersion = 5
	cmdConnect   = 1
	atypIPv4     = 1
	atypDomain   = 3
	atypIPv6     = 4
)

type ContextDialer interface {
	Dial(network, address string) (net.Conn, error)
}
type SOCKS5Server struct {
	Listener net.Listener
	Dialer   ContextDialer
}

func (s *SOCKS5Server) Serve() error {
	if s.Listener == nil || s.Dialer == nil {
		return errors.New("SOCKS5 listener and dialer are required")
	}
	for {
		c, e := s.Listener.Accept()
		if e != nil {
			return e
		}
		go s.handle(c)
	}
}
func (s *SOCKS5Server) handle(client net.Conn) {
	defer client.Close()
	_ = client.SetDeadline(time.Now().Add(30 * time.Second))
	var g [2]byte
	if _, e := io.ReadFull(client, g[:]); e != nil || g[0] != socksVersion {
		return
	}
	methods := make([]byte, int(g[1]))
	if _, e := io.ReadFull(client, methods); e != nil {
		return
	}
	noAuth := false
	for _, m := range methods {
		if m == 0 {
			noAuth = true
			break
		}
	}
	if !noAuth {
		_, _ = client.Write([]byte{5, 255})
		return
	}
	if _, e := client.Write([]byte{5, 0}); e != nil {
		return
	}
	var req [4]byte
	if _, e := io.ReadFull(client, req[:]); e != nil {
		return
	}
	if req[0] != 5 || req[1] != cmdConnect || req[2] != 0 {
		writeReply(client, 7)
		return
	}
	host, e := readAddress(client, req[3])
	if e != nil {
		writeReply(client, 8)
		return
	}
	var pb [2]byte
	if _, e = io.ReadFull(client, pb[:]); e != nil {
		return
	}
	target := net.JoinHostPort(host, strconv.Itoa(int(binary.BigEndian.Uint16(pb[:]))))
	_ = client.SetDeadline(time.Time{})
	upstream, e := s.Dialer.Dial("tcp", target)
	if e != nil {
		writeReply(client, 5)
		return
	}
	defer upstream.Close()
	if _, e = client.Write([]byte{5, 0, 0, 1, 0, 0, 0, 0, 0, 0}); e != nil {
		return
	}
	done := make(chan struct{}, 2)
	go func() { _, _ = io.Copy(upstream, client); done <- struct{}{} }()
	go func() { _, _ = io.Copy(client, upstream); done <- struct{}{} }()
	<-done
}
func readAddress(r io.Reader, atyp byte) (string, error) {
	switch atyp {
	case atypIPv4:
		b := make([]byte, 4)
		if _, e := io.ReadFull(r, b); e != nil {
			return "", e
		}
		return net.IP(b).String(), nil
	case atypIPv6:
		b := make([]byte, 16)
		if _, e := io.ReadFull(r, b); e != nil {
			return "", e
		}
		return net.IP(b).String(), nil
	case atypDomain:
		var n [1]byte
		if _, e := io.ReadFull(r, n[:]); e != nil {
			return "", e
		}
		b := make([]byte, int(n[0]))
		if _, e := io.ReadFull(r, b); e != nil {
			return "", e
		}
		return string(b), nil
	default:
		return "", fmt.Errorf("unsupported SOCKS address type %d", atyp)
	}
}
func writeReply(w io.Writer, code byte) { _, _ = w.Write([]byte{5, code, 0, 1, 0, 0, 0, 0, 0, 0}) }
