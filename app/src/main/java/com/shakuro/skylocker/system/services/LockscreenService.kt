package com.shakuro.skylocker.system.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.shakuro.skylocker.R

class LockscreenService : Service() {

    private var mReceiver: BroadcastReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        mReceiver = LockscreenIntentReceiver()
        registerReceiver(mReceiver, filter)
    }

    // Register for Lockscreen event intents
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return Service.START_STICKY
    }

    // Run service in foreground so it is less likely to be killed by system
    private fun startForeground() {
        val notification = NotificationCompat.Builder(this)
                .setContentTitle(resources.getString(R.string.app_name))
                .setTicker(resources.getString(R.string.app_name))
                .setContentText("Running ${this.application.javaClass.name}")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(null)
                .setOngoing(true)
                .build()
        startForeground(9999, notification)
    }

    // Unregister receiver
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }
}
