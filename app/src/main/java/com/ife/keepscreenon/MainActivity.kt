package com.ife.keepscreenon

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val REQUEST_ENABLE_BLUETOOTH = 1

    private val broadcastReciever = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action

            val bluetoothDevice: BluetoothDevice? = intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

            when (action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    Toast.makeText(applicationContext, "Connected to ${bluetoothDevice?.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filterDeviceConnected = IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        registerReceiver(broadcastReciever, filterDeviceConnected)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter != null) {
            // Device has bluetooth support
            if(!bluetoothAdapter.isEnabled){
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_ENABLE_BLUETOOTH -> {
                when(resultCode)
                {
                    Activity.RESULT_OK -> {
                        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}
