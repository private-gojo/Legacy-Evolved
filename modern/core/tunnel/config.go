package tunnel

import (
	"encoding/json"
	"errors"
	"fmt"
	"net"
	"os"
	"strings"
)

type AuthConfig struct {
	Password       string `json:"password,omitempty"`
	PrivateKeyPEM  string `json:"private_key_pem,omitempty"`
	PrivateKeyPass string `json:"private_key_passphrase,omitempty"`
}
type SSHConfig struct {
	Address              string     `json:"address"`
	Port                 uint16     `json:"port"`
	Username             string     `json:"username"`
	Auth                 AuthConfig `json:"auth"`
	HostKeyPolicy        string     `json:"host_key_policy"`
	KnownHostsPath       string     `json:"known_hosts_path,omitempty"`
	LocalSOCKSAddress    string     `json:"local_socks_address"`
	ConnectTimeoutSecond int        `json:"connect_timeout_seconds"`
}

func ParseSSHConfig(data []byte) (SSHConfig, error) {
	var c SSHConfig
	if err := json.Unmarshal(data, &c); err != nil {
		return c, fmt.Errorf("decode SSH config: %w", err)
	}
	return c, c.Validate()
}
func (c SSHConfig) Validate() error {
	if strings.TrimSpace(c.Address) == "" || c.Port == 0 {
		return errors.New("SSH address and port are required")
	}
	if strings.TrimSpace(c.Username) == "" {
		return errors.New("SSH username is required")
	}
	if c.Auth.Password == "" && c.Auth.PrivateKeyPEM == "" {
		return errors.New("SSH password or private key is required")
	}
	if _, _, err := net.SplitHostPort(c.LocalSOCKSAddress); err != nil {
		return errors.New("local SOCKS address must be host:port")
	}
	if c.HostKeyPolicy != "insecure" && c.HostKeyPolicy != "known_hosts" {
		return errors.New(`host_key_policy must be "insecure" or "known_hosts"`)
	}
	if c.HostKeyPolicy == "known_hosts" && c.KnownHostsPath == "" {
		return errors.New("known_hosts_path is required for known_hosts policy")
	}
	return nil
}
func LoadSSHConfig(path string) (SSHConfig, error) {
	b, e := os.ReadFile(path)
	if e != nil {
		return SSHConfig{}, e
	}
	return ParseSSHConfig(b)
}
