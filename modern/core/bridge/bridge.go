package bridge

import (
	"github.com/private-gojo/Legacy-Evolved/core/tunnel"
	"sync"
)

var mu sync.Mutex
var engine *tunnel.Engine

// StartSSHDirect starts SSH and the local SOCKS5 CONNECT endpoint. Empty string means success.
func StartSSHDirect(configJSON string) string {
	c, e := tunnel.ParseSSHConfig([]byte(configJSON))
	if e != nil {
		return e.Error()
	}
	mu.Lock()
	defer mu.Unlock()
	if engine != nil {
		return "tunnel engine already initialized"
	}
	n := tunnel.NewEngine()
	if e = n.Start(c); e != nil {
		return e.Error()
	}
	engine = n
	return ""
}

// StopTunnel stops the SSH connection and local SOCKS5 listener. Empty string means success.
func StopTunnel() string {
	mu.Lock()
	defer mu.Unlock()
	if engine == nil {
		return ""
	}
	e := engine.Stop()
	engine = nil
	if e != nil {
		return e.Error()
	}
	return ""
}
