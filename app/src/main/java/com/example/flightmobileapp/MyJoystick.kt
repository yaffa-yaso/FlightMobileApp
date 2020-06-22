package com.example.flightmobileapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.sqrt


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
        baseRadius = width.coerceAtMost(height) / 3.toFloat()
        hatRadius = width.coerceAtMost(height) / 7.toFloat()
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
            val myCanvas: Canvas = this.holder.lockCanvas() //Stuff to draw
            val colors = Paint()
            //Draw the base first before shading
            colors.setARGB(255, 32, 32, 32)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)

            colors.setARGB(255, 95,95,95)
            myCanvas.drawCircle(centerX, centerY, hatRadius, colors)

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
                val inLim: Float = sqrt(x1 * x1 + y1 * y1)
                var knobPositionX: Float = x1
                var knobPositionY: Float = y1

                if (inLim < baseRadius/2) {
                    knobPositionX = x1
                    knobPositionY = y1
                } else {
                    if (x1 == 0f) {
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

                        knobPositionX = if(x1 > 0) {
                            ((-b + sqrt(b * b - 4 * a * c)) / (2 * a)).toFloat()
                        } else {
                            ((-b - sqrt(b * b - 4 * a * c)) / (2 * a)).toFloat()
                        }
                        knobPositionY = (m * (knobPositionX - x1) + y1).toFloat()
                    }
                }
                drawJoystick(knobPositionX, knobPositionY)
                joystickCallback!!.onJoystickMoved((knobPositionX - centerX)/baseRadius,
                    (knobPositionY - centerY)/baseRadius, getId());
            } else {
                drawJoystick(centerX, centerY)
                joystickCallback!!.onJoystickMoved(0f, 0f, id)
            }
        }
        return true
    }
}