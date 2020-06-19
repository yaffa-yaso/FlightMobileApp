package com.example.flightmobileapp

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener


class MyJoystick : SurfaceView, SurfaceHolder.Callback, OnTouchListener {
    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var hatRadius = 0f
    private var joystickCallback: JoystickListener? = null
    private val ratio = 5 //The smaller, the more shading will occur
    private fun setupDimensions() {
        centerX = width / 2.toFloat()
        centerY = height / 2.toFloat()
        baseRadius = Math.min(width, height) / 3.toFloat()
        hatRadius = Math.min(width, height) / 5.toFloat()
    }

    constructor(context: Context?) : super(context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback =
            context
    }

    constructor(
        context: Context?,
        attributes: AttributeSet?,
        style: Int
    ) : super(context, attributes, style) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback =
            context
    }

    constructor(context: Context?, attributes: AttributeSet?) : super(
        context,
        attributes
    ) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback =
            context
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            val myCanvas = this.holder.lockCanvas() //Stuff to draw
            val colors = Paint()
            myCanvas.drawColor(
                Color.TRANSPARENT,
                PorterDuff.Mode.CLEAR
            ) // Clear the BG

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            val hypotenuse = Math.sqrt(
                Math.pow(
                    newX - centerX.toDouble(),
                    2.0
                ) + Math.pow(newY - centerY.toDouble(), 2.0)
            ).toFloat()
            val sin = (newY - centerY) / hypotenuse //sin = o/h
            val cos = (newX - centerX) / hypotenuse //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 100, 100, 100)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            for (i in 1..(baseRadius / ratio).toInt()) {
                colors.setARGB(
                    150 / i,
                    255,
                    0,
                    0
                ) //Gradually decrease the shade of black drawn to create a nice shading effect
                myCanvas.drawCircle(
                    newX - cos * hypotenuse * (ratio / baseRadius) * i,
                    newY - sin * hypotenuse * (ratio / baseRadius) * i,
                    i * (hatRadius * ratio / baseRadius),
                    colors
                ) //Gradually increase the size of the shading effect
            }

            //Drawing the joystick hat
            for (i in 0..(hatRadius / ratio).toInt()) {
                colors.setARGB(
                    220,
                    (i * (220 * ratio / hatRadius)).toInt(),
                    (i * (220 * ratio / hatRadius)).toInt(),
                    220
                ) //Change the joystick color for shading purposes
                myCanvas.drawCircle(
                    newX,
                    newY,
                    hatRadius - i.toFloat() * ratio / 2,
                    colors
                ) //Draw the shading for the hat
            }
            holder.unlockCanvasAndPost(myCanvas) //Write the new drawing to the SurfaceView
        }
    }

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


    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if (v == this) {
            if (e.action != MotionEvent.ACTION_UP) {
                val x1: Float = e.rawX - MotionEvent.AXIS_X
                val y1: Float = e.rawY - MotionEvent.AXIS_Y
                val inLim: Float = Math.sqrt(x1 * x1 + y1 * y1.toDouble()).toFloat()
                var knobPositionX: Float = x1
                var knobPositionY: Float = y1

                if (inLim < baseRadius/2) {
                    knobPositionX = x1
                    knobPositionY = y1
                } else {
                    if (x1 === 0f) {
                        knobPositionY = baseRadius/2
                        if (y1 < 0) {
                            knobPositionY *= -1
                        }
                    } else {
                        val m = (y1 / x1).toDouble()
                        val a = m * m + 1
                        val b = 2 * m * y1 - 2 * m * m * x1
                        val c: Double =
                            m * m * x1 * x1 - 2 * m * y1 * x1 + y1 * y1 - baseRadius * baseRadius/4
                        if (x1 > 0) {
                            knobPositionX = ((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a)).toFloat()
                        } else {
                            knobPositionX = ((-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a)).toFloat()
                        }
                        knobPositionY = (m * (knobPositionX - x1) + y1).toFloat()
                    }
                }
                drawJoystick(knobPositionX, knobPositionY)
            } else {
                drawJoystick(centerX, centerY)
                joystickCallback!!.onJoystickMoved(0f, 0f, id)
            }
        }
        return true
    }
}