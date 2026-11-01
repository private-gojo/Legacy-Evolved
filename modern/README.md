# Power-on VPN — Modern Rewrite Foundation

This rewrite is isolated under `modern/`; the existing legacy project on `main` is unchanged.

## Current implementation
- Kotlin Android application scaffold.
- Go SSH Direct core using `golang.org/x/crypto/ssh`.
- Password and private-key authentication.
- Local SOCKS5 CONNECT endpoint over the SSH client.
- Configurable host-key policy: `insecure` (legacy-compatible trust-all) or `known_hosts`.
- Go lifecycle API prepared for a later gomobile/JNI binding.
- Go unit tests for SSH configuration parsing and validation.

## Not implemented yet
HEV TUN packet processing, Android-Go binding, payload macros, HTTP CONNECT proxy, TLS wrapper, WebSocket, UDP relay, profile migration/import/export, and optional torrent detection UI/list updates.

**The current APK is a build/install smoke test only, not a working VPN.** The app deliberately does not request VPN permission or establish a TUN interface, preventing traffic blackholing until HEV and the Go bridge are integrated.

## Go tests
```sh
cd modern/core
go test ./...
```

## Android build
```sh
cd modern/android
gradle :app:assembleDebug
```
