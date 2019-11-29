package com.ife.keepscreenon

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast

class IntentServiceScreenOn : IntentService("Keep Screen On Toast Service") {

    override fun onHandleIntent(intent: Intent?) {
//        Toast.makeText(this, "Alarm triggered task fired", Toast.LENGTH_SHORT).show()
        // TODO code to keep screen awake
        Log.d(this.javaClass.simpleName, "Alarm triggered task fired")
    }
}