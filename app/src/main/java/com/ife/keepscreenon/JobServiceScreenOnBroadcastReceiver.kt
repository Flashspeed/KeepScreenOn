package com.ife.keepscreenon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class JobServiceScreenOnBroadcastReceiver: BroadcastReceiver() {

    object codes{
        const val REQUEST_CODE = 8888
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentService = Intent(context, JobServiceScreenOn::class.java)
        context?.startService(intentService)
    }
}