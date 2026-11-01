# CI/CD

The root workflow `.github/workflows/android-ci.yml` runs Go unit tests and builds the modern Android debug APK when files under `modern/` change, on pull requests targeting `main`, or by manual dispatch.

## Download the APK

1. Open the repository's **Actions** tab.
2. Select the latest **Modern Rewrite CI** run.
3. Confirm both **Go core tests** and **Build debug APK** pass.
4. Download `poweronvpn-modern-debug-<run number>` under **Artifacts**.
5. Extract the artifact ZIP and install the APK on Android.

The APK is a build/install smoke test only. It does not provide a working VPN until HEV TUN processing and the Android-Go bridge are integrated.
