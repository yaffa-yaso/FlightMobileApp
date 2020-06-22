package com.example.flightmobileapp

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Created by Daniel on 7/25/2016.
 */
class MyJoystick : SurfaceView, SurfaceHolder.Callback, OnTouchListener {
    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var hatRadius = 0f
    private var joystickCallback: JoystickListener? = null
    private val ratio = 2 //The smaller, the more shading will occur
    private fun setupDimensions() {
        centerX = width / 2.toFloat()
        centerY = height / 2.toFloat()
        baseRadius = width.coerceAtMost(height) / 3.toFloat()
        hatRadius = width.coerceAtMost(height) / 7.toFloat()
    }

    constructor(context: Context?) : super(context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    constructor(
        context: Context?,
        attributes: AttributeSet?,
        style: Int
    ) : super(context, attributes, style) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    constructor(context: Context?, attributes: AttributeSet?) : super(
        context,
        attributes
    ) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            val myCanvas = this.holder.lockCanvas() //Stuff to draw
            val colors = Paint()
            myCanvas.drawColor(
                Color.TRANSPARENT,
                PorterDuff.Mode.CLEAR
            ) // Clear the BG
            myCanvas.drawRGB(225,225,225)

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            val hypotenuse = sqrt(
                (newX - centerX.toDouble()).pow(2.0) + (newY - centerY.toDouble()).pow(2.0)
            ).toFloat()
            val sin = (newY - centerY) / hypotenuse //sin = o/h
            val cos = (newX - centerX) / hypotenuse //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 40, 40, 40)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)

            //Drawing the joystick hat

            //Drawing the joystick hat
            for (i in 0..(hatRadius / ratio).toInt()) {
                colors.setARGB(
                    255,
                    70 + 2*i,
                    70 + 2*i,
                    70 + 2*i
                ) //Change the joystick color for shading purposes
                myCanvas.drawCircle(
                    newX,
                    newY,
                    hatRadius - i.toFloat() * ratio,
                    colors
                ) //Draw the shading for the hat
            }
            holder.unlockCanvasAndPost(myCanvas) //Write the new drawing to the SurfaceView
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if (v == this) {
            if (e.action != MotionEvent.ACTION_UP) {
                val displacement = sqrt(
                    (e.x - centerX.toDouble()).pow(2.0) + (e.y - centerY.toDouble()).pow(2.0)
                ).toFloat()
                if (displacement < baseRadius) {
                    drawJoystick(e.x, e.y)
                    joystickCallback!!.onJoystickMoved(
                        (e.x - centerX) / baseRadius,
                        (e.y - centerY) / baseRadius,
                        id
                    )
                } else {
                    val ratio = baseRadius / displacement
                    val constrainedX = centerX + (e.x - centerX) * ratio
                    val constrainedY = centerY + (e.y - centerY) * ratio
                    drawJoystick(constrainedX, constrainedY)
                    joystickCallback!!.onJoystickMoved(
                        (constrainedX - centerX) / baseRadius,
                        (constrainedY - centerY) / baseRadius,
                        id
                    )
                }
            } else drawJoystick(centerX, centerY)
            joystickCallback!!.onJoystickMoved(0f, 0f, id)
        }
        return true
    }
}