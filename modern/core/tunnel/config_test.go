package tunnel

import "testing"

func TestParseSSHConfig(t *testing.T) {
	b := []byte(`{"address":"127.0.0.1","port":22,"username":"tester","auth":{"password":"test"},"host_key_policy":"insecure","local_socks_address":"127.0.0.1:10808"}`)
	c, e := ParseSSHConfig(b)
	if e != nil {
		t.Fatal(e)
	}
	if c.LocalSOCKSAddress != "127.0.0.1:10808" {
		t.Fatalf("unexpected SOCKS address: %s", c.LocalSOCKSAddress)
	}
}
func TestParseSSHConfigRejectsMissingAuth(t *testing.T) {
	b := []byte(`{"address":"127.0.0.1","port":22,"username":"tester","host_key_policy":"insecure","local_socks_address":"127.0.0.1:10808"}`)
	if _, e := ParseSSHConfig(b); e == nil {
		t.Fatal("expected missing auth error")
	}
}
