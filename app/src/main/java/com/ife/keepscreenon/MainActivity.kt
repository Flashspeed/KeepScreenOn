package com.ife.keepscreenon

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BluetoothBroadcastReceiver.IBluetoothResult {

    override fun onMessageAvailable(message: String) {
        txtBluetoothStatus.text = message
    }

    private val REQUEST_ENABLE_BLUETOOTH = 1

    private val broadcastReceiver = BluetoothBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceivers()
        if(deviceHasBluetoothSupport()){
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            // Check if bluetooth is on
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

    private fun deviceHasBluetoothSupport(): Boolean{
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null
    }

    private fun registerReceivers(){
        val filterDeviceConnected = IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        val filterDeviceDisconnected = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        val filterDeviceStateChanged = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(broadcastReceiver, filterDeviceConnected)
        registerReceiver(broadcastReceiver, filterDeviceDisconnected)
        registerReceiver(broadcastReceiver, filterDeviceStateChanged)
    }


}
