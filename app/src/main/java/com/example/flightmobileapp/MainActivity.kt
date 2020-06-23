package com.example.flightmobileapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        localhost1.setOnClickListener {
            urlText.setText(localhost1.text)
        }
        localhost2.setOnClickListener {
            urlText.setText(localhost2.text)
        }
        localhost3.setOnClickListener {
            urlText.setText(localhost3.text)
        }
        localhost4.setOnClickListener {
            urlText.setText(localhost4.text)
        }
        localhost5.setOnClickListener {
            urlText.setText(localhost5.text)
        }

        goToControlScreen.setOnClickListener {
            val preferences = getSharedPreferences("URLdb", Context.MODE_PRIVATE)
            preferences.edit().apply {
                putString("savedURL", urlText.text.toString())
            }.apply()

            localhost5.text = localhost4.text
            localhost4.text = localhost3.text
            localhost3.text = localhost2.text
            localhost2.text = localhost1.text
            localhost1.text = preferences.getString("savedURL", "URL")

            startActivity(Intent(this, ControlScreenActivity::class.java))
        }
    }
}