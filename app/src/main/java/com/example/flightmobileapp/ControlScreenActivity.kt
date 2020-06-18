package com.example.flightmobileapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_screen.*

class ControlScreenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_screen)

        val bitmap: Bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        var joystick = MyJoystick(this)
        joystick.draw(canvas)

        /*var shapeDrawable: ShapeDrawable*/

//        // rectangle positions
//        var left = 200
//        var top = 650
//        var right = 500
//        var bottom = 900
//
//        // draw oval shape to canvas
//        shapeDrawable = ShapeDrawable(OvalShape())
//        shapeDrawable.setBounds( left, top, right, bottom)
//        shapeDrawable.getPaint().setColor(Color.parseColor("darkgrey"))
//        shapeDrawable.draw(canvas)


        // now bitmap holds the updated pixels

        // set bitmap as background to ImageView
//        imageV.background = BitmapDrawable(getResources(), bitmap)

    }
}