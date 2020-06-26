package com.example.flightmobileapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.control_screen.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ControlScreenActivity: AppCompatActivity(), JoystickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyJoystick(this)

        val json = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:44310/")
            .addConverterFactory(GsonConverterFactory.create(json))
            .build()

        val api = retrofit.create(Api::class.java)

        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bytes = response.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(bytes)
                runOnUiThread {
                    imageV.setImageBitmap(bitmap)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                return
            }
        })

        setContentView(R.layout.control_screen)

        rudderSeekBar.setOnSeekBarChangeListener(rudderText, rudderSeekBar.progress)
        throttleSeekBar.setOnSeekBarChangeListener(throttleText, throttleSeekBar.progress)

    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.d("Joystick", "X percent: $xPercent Y percent: $yPercent")
    }
}

private fun SeekBar.setOnSeekBarChangeListener(text: TextView?, i: Number) {
    if (text != null) {
        text.text = i.toString()
    }
}



