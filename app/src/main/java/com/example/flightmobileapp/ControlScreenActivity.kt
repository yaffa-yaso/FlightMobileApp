package com.example.flightmobileapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.telecom.Call
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import javax.security.auth.callback.Callback

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_screen.*
import retrofit2.Response

class ControlScreenActivity: AppCompatActivity(), JoystickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val joystick = MyJoystick(this)
        val s: Editable? = (findViewById<View>(R.id.urlText) as EditText).text
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java, s.toString())
        val call = request.getImage()

        call.enqueue(object : retrofit2.Callback<SimulatorImage>{
            override fun onResponse(call: retrofit2.Call<SimulatorImage>, response: Response<SimulatorImage>) {
                if (response.isSuccessful){
                    progress_bar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ControlScreenActivity)
                        adapter = ImagesAdapter(response.body()!!.results)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<SimulatorImage>, t: Throwable) {
                Toast.makeText(this@ControlScreenActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        setContentView(R.layout.control_screen)
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.d("Joystick", "X percent: $xPercent Y percent: $yPercent")
    }
}