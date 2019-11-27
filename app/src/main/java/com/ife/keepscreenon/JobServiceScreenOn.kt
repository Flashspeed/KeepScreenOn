package com.ife.keepscreenon

import android.app.IntentService
import android.content.Intent
import android.util.Log

class JobServiceScreenOn : IntentService("Keep Screen On Toast Service") {

//    override suspend fun doWork(): Result {
//        Toast.makeText(context, "Work manager task fired", Toast.LENGTH_SHORT).show()
//        return Result.success()
//    }

    override fun onHandleIntent(intent: Intent?) {
//        Toast.makeText(applicationContext, "Alarm triggered task fired", Toast.LENGTH_SHORT).show()
        Log.d(this.javaClass.simpleName, "Alarm triggered task fired")
    }
}