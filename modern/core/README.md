# Go core: first slice

SSH Direct + local SOCKS5 CONNECT. Password and private-key authentication are supported.

Example config:
```json
{
  "address": "ssh.example.net",
  "port": 22,
  "username": "account",
  "auth": { "password": "replace-me" },
  "host_key_policy": "insecure",
  "local_socks_address": "127.0.0.1:10808",
  "connect_timeout_seconds": 15
}
```

Use `host_key_policy: "known_hosts"` with `known_hosts_path` for host-key verification.

Later Android binding command (not yet built or integrated):
```sh
gomobile bind -target=android -androidapi 24 -o ../android/app/libs/tunnel-core.aar ./bridge
```
