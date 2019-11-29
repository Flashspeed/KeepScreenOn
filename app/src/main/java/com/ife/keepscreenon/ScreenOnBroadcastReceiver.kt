package com.ife.keepscreenon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenOnBroadcastReceiver : BroadcastReceiver() {

    object codes {
        const val REQUEST_CODE = 8888
    }

    private lateinit var context: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context!!
        val intentService = Intent(context, IntentServiceScreenOn::class.java)

        try {
            context.startForegroundService(intentService)
            context.startService(intentService)
            Log.d(this.javaClass.simpleName, "Service Started")
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error on startService() because: ${e.message}")
        }


    }
}