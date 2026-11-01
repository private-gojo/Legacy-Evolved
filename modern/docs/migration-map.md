# Legacy migration map

| Legacy feature | Destination | Status |
|---|---|---|
| SSH Direct | Go tunnel package | Initial CONNECT path |
| SSH password/public key | Go SSH auth | Implemented |
| SSH custom payload | Go payload package | Pending |
| SSH via HTTP proxy | Go HTTP CONNECT transport | Pending |
| SSH over SSL/TLS | Go TLS transport | Pending |
| Payload + SSL/TLS | Go payload + TLS | Pending |
| Local SOCKS5 | Go SOCKS5 server | CONNECT only |
| Android TUN | Kotlin VpnService | Scaffold only |
| tun2socks replacement | HEV/C | Pending |
| DNS/UDP | HEV + compatible upstream relay | Pending; legacy resolver port is not proof of UDP relay |
| Torrent detection | Kotlin setting + package list | Pending; disabled by default |
| `.sks` profiles | Migration/import module | Pending |

Compatibility rules: preserve payload tokens only after fixture tests; keep TLS verification configurable; never infer general UDP support from DNS resolver configuration; keep original Legacy-Evolved source unchanged.
