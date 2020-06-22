package com.example.flightmobileapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ControlScreenActivity: AppCompatActivity(), JoystickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val joystick = MyJoystick(this)
        setContentView(R.layout.control_screen)
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.d("Joystick", "X percent: $xPercent Y percent: $yPercent")
    }
}