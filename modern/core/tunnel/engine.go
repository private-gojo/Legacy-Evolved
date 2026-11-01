package tunnel

import (
	"errors"
	"fmt"
	"golang.org/x/crypto/ssh"
	"golang.org/x/crypto/ssh/knownhosts"
	"net"
	"sync"
	"time"
)

type Engine struct {
	mu       sync.Mutex
	client   *ssh.Client
	listener net.Listener
}

func NewEngine() *Engine { return &Engine{} }
func (e *Engine) Start(c SSHConfig) error {
	if err := c.Validate(); err != nil {
		return err
	}
	e.mu.Lock()
	defer e.mu.Unlock()
	if e.client != nil || e.listener != nil {
		return errors.New("tunnel engine is already running")
	}
	auth, err := buildAuth(c.Auth)
	if err != nil {
		return err
	}
	hostKey := ssh.InsecureIgnoreHostKey()
	if c.HostKeyPolicy == "known_hosts" {
		hostKey, err = knownhosts.New(c.KnownHostsPath)
		if err != nil {
			return fmt.Errorf("load known_hosts: %w", err)
		}
	}
	timeout := time.Duration(c.ConnectTimeoutSecond) * time.Second
	if timeout <= 0 {
		timeout = 15 * time.Second
	}
	sc := &ssh.ClientConfig{User: c.Username, Auth: auth, HostKeyCallback: hostKey, Timeout: timeout}
	remote := net.JoinHostPort(c.Address, fmt.Sprintf("%d", c.Port))
	client, err := ssh.Dial("tcp", remote, sc)
	if err != nil {
		return fmt.Errorf("SSH connect: %w", err)
	}
	ln, err := net.Listen("tcp", c.LocalSOCKSAddress)
	if err != nil {
		_ = client.Close()
		return fmt.Errorf("listen SOCKS5: %w", err)
	}
	e.client, e.listener = client, ln
	go func() { _ = (&SOCKS5Server{Listener: ln, Dialer: sshDialer{client}}).Serve() }()
	return nil
}
func (e *Engine) Stop() error {
	e.mu.Lock()
	defer e.mu.Unlock()
	var first error
	if e.listener != nil {
		if err := e.listener.Close(); err != nil && !errors.Is(err, net.ErrClosed) {
			first = err
		}
	}
	if e.client != nil {
		if err := e.client.Close(); err != nil && first == nil {
			first = err
		}
	}
	e.client, e.listener = nil, nil
	return first
}

type sshDialer struct{ client *ssh.Client }

func (d sshDialer) Dial(n, a string) (net.Conn, error) { return d.client.Dial(n, a) }
func buildAuth(c AuthConfig) ([]ssh.AuthMethod, error) {
	out := make([]ssh.AuthMethod, 0, 2)
	if c.Password != "" {
		out = append(out, ssh.Password(c.Password))
	}
	if c.PrivateKeyPEM != "" {
		var s ssh.Signer
		var e error
		if c.PrivateKeyPass != "" {
			s, e = ssh.ParsePrivateKeyWithPassphrase([]byte(c.PrivateKeyPEM), []byte(c.PrivateKeyPass))
		} else {
			s, e = ssh.ParsePrivateKey([]byte(c.PrivateKeyPEM))
		}
		if e != nil {
			return nil, fmt.Errorf("parse SSH private key: %w", e)
		}
		out = append(out, ssh.PublicKeys(s))
	}
	if len(out) == 0 {
		return nil, errors.New("no SSH authentication method configured")
	}
	return out, nil
}
