package com.poweronvpn.vpn

import android.net.VpnService

class PowerOnVpnService : VpnService() {
    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        stopSelf(startId)
        return START_NOT_STICKY
    }
}
