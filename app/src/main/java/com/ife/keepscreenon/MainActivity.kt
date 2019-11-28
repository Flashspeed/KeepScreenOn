package com.ife.keepscreenon

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), BluetoothBroadcastReceiver.IBluetoothResult,
    CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onMessageAvailable(message: String) {
        txtBluetoothStatus.text = message
    }

    private val REQUEST_ENABLE_BLUETOOTH = 1

    private val broadcastReceiver = BluetoothBroadcastReceiver()
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onResume() {
        super.onResume()

        registerReceivers()
        checkForConnection()
    }

    private fun checkForConnection(){
        Log.d(this.javaClass.simpleName, "In check for connection")

        if(deviceHasBluetoothSupport()){
            val pairedDevices = bluetoothAdapter.bondedDevices
            pairedDevices.forEach { device ->
                val deviceName = device.name

                if(device?.bluetoothClass?.deviceClass == BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE){
                    Toast.makeText(this, "Resume - Connected to handsfree (Car) audio in $deviceName", Toast.LENGTH_LONG).show()
                }

                if (deviceName == "Talk2") {
                    Log.d(this.javaClass.simpleName, "Paired Device: $deviceName")
                    val UUID = device.uuids[0].uuid

                    launch {
                        withContext(Dispatchers.IO) {
                            try {
                                bluetoothAdapter.cancelDiscovery()
                                device.createRfcommSocketToServiceRecord(UUID).connect()
                                Log.d(this.javaClass.simpleName, "checkForConnection - Connection to $deviceName Successful")

                                launch {
                                    withContext(Dispatchers.Main){
                                        txtBluetoothStatus.text = "Resume - Connected to $deviceName"
                                    }
                                }

                            } catch (e: IOException) {
                                Log.e(
                                    this.javaClass.simpleName,
                                    "Could not connect because: ${e.message}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceivers()

        if (deviceHasBluetoothSupport()) {
            // Check if bluetooth is on
            if (!bluetoothAdapter.isEnabled) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
//                checkForConnection()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_ENABLE_BLUETOOTH -> {
                when (resultCode) {
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

    private fun deviceHasBluetoothSupport(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null
    }

    private fun registerReceivers() {
        val filterDeviceConnected = IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        val filterDeviceDisconnected = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        val filterDeviceStateChanged = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(broadcastReceiver, filterDeviceConnected)
        registerReceiver(broadcastReceiver, filterDeviceDisconnected)
        registerReceiver(broadcastReceiver, filterDeviceStateChanged)
    }

    private fun bluetoothIsConnected(): Boolean {
        return bluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED
    }


}
