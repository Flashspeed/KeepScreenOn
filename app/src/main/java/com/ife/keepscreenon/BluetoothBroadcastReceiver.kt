package com.ife.keepscreenon

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BluetoothBroadcastReceiver: BroadcastReceiver(){

    interface IBluetoothResult {
        fun onMessageAvailable(message: String)
    }
    private var messageAvailableListener: IBluetoothResult? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action
        messageAvailableListener = context as? IBluetoothResult

        val bluetoothDevice: BluetoothDevice? = intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        when (action) {

            BluetoothDevice.ACTION_FOUND -> {
                Toast.makeText(context, "Found ${bluetoothDevice?.name}", Toast.LENGTH_SHORT).show()
            }

            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                messageAvailableListener?.onMessageAvailable("Connected to ${bluetoothDevice?.name}")

                Log.d(this.javaClass.simpleName, "Connected to ${bluetoothDevice?.name}")

                if(bluetoothDevice?.bluetoothClass?.deviceClass == BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO){
                    Toast.makeText(context, "Connected to car audio", Toast.LENGTH_SHORT).show()
                    Log.d(this.javaClass.simpleName, "Connected to car audio")
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                messageAvailableListener?.onMessageAvailable("Bluetooth disconnected from ${bluetoothDevice?.name}")
                Toast.makeText(context, "Bluetooth disconnected from ${bluetoothDevice?.name}", Toast.LENGTH_SHORT).show()
                Log.d(this.javaClass.simpleName, "Bluetooth disconnected from ${bluetoothDevice?.name}")

            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                when (bluetoothDevice?.bondState) {
                    BluetoothDevice.BOND_BONDED -> {}
                    BluetoothDevice.BOND_BONDING -> {}
                    BluetoothDevice.BOND_NONE -> {}
                }
            }
        }
    }
}