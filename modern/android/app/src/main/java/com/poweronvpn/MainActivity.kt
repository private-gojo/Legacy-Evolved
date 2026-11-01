package com.poweronvpn

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status = TextView(this).apply {
            text = "Power-on VPN\nModern rewrite foundation\n\nBuild validation only. VPN traffic is not enabled yet.\nHEV TUN processing and the Android-Go bridge are pending."
            textSize = 16f
            setPadding(32, 48, 32, 24)
        }
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(status)
        })
    }
}
