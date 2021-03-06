package com.shakuro.skylocker.system

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.shakuro.skylocker.system.services.LockscreenService
import javax.inject.Inject

class LockServiceManager @Inject constructor(private val context: Context) {

    fun startLockService() {
        context.startService(Intent(context, LockscreenService::class.java))
    }

    fun stopLockService() {
        context.stopService(Intent(context, LockscreenService::class.java))
    }

    fun isLockServiceActive(): Boolean {
        var result = false
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if ((LockscreenService::class.java).name == service.service.className) {
                result = true
                break
            }
        }
        return result
    }
}