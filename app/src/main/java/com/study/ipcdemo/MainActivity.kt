package com.study.ipcdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.ipc.Ipc
import com.study.ipcdemo.location.LocationManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Ipc.register(LocationManager::class.java)
    }
}
