package com.example.flightmobileapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        goToControlScreen.setOnClickListener {
            startActivity(Intent(this, ControlScreenActivity::class.java))
        }
    }

    override fun onClick(v: View) {
        /*if(v.id == R.id.localhost1) {
            R.id.URLText.
        }else if(v.id == R.id.localhost2){
            btn2.setText("X");
        }else if(v.id == R.id.localhost3){
            btn2.setText("X");
        }else if(v.id == R.id.localhost4){
            btn2.setText("X");
        }else if(v.id == R.id.localhost5){
            btn2.setText("X");
        }*/
    }
}