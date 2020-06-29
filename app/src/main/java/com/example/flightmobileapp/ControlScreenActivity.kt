package com.example.flightmobileapp

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.control_screen.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.round


class ControlScreenActivity: AppCompatActivity(), JoystickListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val joystick = MyJoystick(this)

        val json = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create(json))
            .build()

        val api = retrofit.create(Api::class.java)

        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    println("response is successful")
                    val bytes = response.body()?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(bytes)
                    runOnUiThread {
                        while (true) {
                            imageV.setImageBitmap(bitmap)
                        }
                    }
                } else{
                    println("an error occured")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               println("failed in getting image $call ,--- $t")
            }
        })

        setContentView(R.layout.control_screen)


        rudderSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                rudderVal.text = (i.toDouble()/100).toString()
                sendPost()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        throttleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                throttleVal.text = (i.toDouble()/100).toString()
                sendPost()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    override fun onJoystickMoved(x: Float, y: Float) {

            elevatorVal.text = String.format("%.2f",x)
            aileronVal.text = String.format("%.2f",y)

        sendPost()
    }

    fun sendPost() {
        val thread = Thread(Runnable {
            try {
                val urlString: String = findViewById<TextView>(R.id.urlText).toString()
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true
                conn.doInput = true
                val jsonParam = JSONObject()
                jsonParam.put("aileron", aileronVal)
                jsonParam.put("rudder", rudderVal)
                jsonParam.put("elevator", elevatorVal)
                jsonParam.put("throttle", throttleVal)
                Log.i("JSON", jsonParam.toString())
                val os = DataOutputStream(conn.outputStream)
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString())
                os.flush()
                os.close()
                Log.i("STATUS", conn.responseCode.toString())
                Log.i("MSG", conn.responseMessage)
                conn.disconnect()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })
        thread.start()
    }
}