package com.study.ipcdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.study.ipc.IPCService
import com.study.ipc.Ipc
import com.study.ipcdemo.location.ILocationManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Ipc.register(LocationManager::class.java)
        Ipc.connect(this, IPCService.IPCService0::class.java)
    }

    fun test(view: View) {
        Ipc.getInstanceWithName(IPCService.IPCService0::class.java, ILocationManager::class.java,"getDefault")
    }
}
