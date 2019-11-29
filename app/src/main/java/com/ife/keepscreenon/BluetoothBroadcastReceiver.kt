package com.ife.keepscreenon

import android.app.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BluetoothBroadcastReceiver : BroadcastReceiver() {

    interface IBluetoothResult {
        fun onMessageAvailable(message: String)
    }

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private var messageAvailableListener: IBluetoothResult? = null

    private lateinit var notificationHelper: NotificationHelper

    private lateinit var context: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context!!
        notificationHelper = NotificationHelper(context)

        val action: String? = intent?.action
        messageAvailableListener = context as? IBluetoothResult

        val bondedBluetoothDevices = bluetoothAdapter.bondedDevices

        val bluetoothDevice: BluetoothDevice? =
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        when (action) {

            BluetoothDevice.ACTION_FOUND -> {
                Toast.makeText(context, "Found ${bluetoothDevice?.name}", Toast.LENGTH_SHORT).show()
            }

            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                messageAvailableListener?.onMessageAvailable("Connected to ${bluetoothDevice?.name}")

                Log.d(this.javaClass.simpleName, "Connected to ${bluetoothDevice?.name}")
                if (!isScreenOnServiceRunning()) {
                    startAlarm()
                    notificationHelper.hideNotification()
                    notificationHelper.showNotification(
                        null,
                        "Screen On",
                        "Screen is on",
                        null
                    )
                }

                if (bluetoothDevice?.bluetoothClass?.deviceClass == BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO) {
                    Toast.makeText(
                        context,
                        "Connected to car audio in ${bluetoothDevice.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(this.javaClass.simpleName, "Connected to car audio")
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                messageAvailableListener?.onMessageAvailable("Bluetooth disconnected from ${bluetoothDevice?.name}")
                Toast.makeText(
                    context,
                    "Bluetooth disconnected from ${bluetoothDevice?.name}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(
                    this.javaClass.simpleName,
                    "Bluetooth disconnected from ${bluetoothDevice?.name}"
                )
                if (isScreenOnServiceRunning()) {
                    cancelAlarm()
                    notificationHelper.hideNotification()
                }
            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                when (bluetoothDevice?.bondState) {
                    BluetoothDevice.BOND_BONDED -> {
                    }
                    BluetoothDevice.BOND_BONDING -> {
                    }
                    BluetoothDevice.BOND_NONE -> {
                    }
                }
            }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, ScreenOnBroadcastReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            ScreenOnBroadcastReceiver.codes.REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startAlarm() {

        val startTime = System.currentTimeMillis()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            60_000L,
            getPendingIntent()
        )

        Toast.makeText(
            context,
            "Alarm started",
            Toast.LENGTH_SHORT
        ).show()
        Log.d(this.javaClass.simpleName, "Alarm Started")
    }

    private fun cancelAlarm() {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent())

        Toast.makeText(
            context,
            "Alarm canceled",
            Toast.LENGTH_SHORT
        ).show()

        Log.d(this.javaClass.simpleName, "Alarm Cancelled")

    }

    private fun isScreenOnServiceRunning(): Boolean {
        val myService = IntentServiceScreenOn::class.java
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(Int.MAX_VALUE).forEach {
            if (myService.name == it.service.className) {
                return true
            }
        }
        return false
    }
}