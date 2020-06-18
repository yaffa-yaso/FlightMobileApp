package com.example.flightmobileapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min


class MyJoystick: View {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#FFC107")
        isAntiAlias = true
    }

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0

    ) : super (context, attrs, defStyleAttr) {

    }


    private var radius: Float = 120f
    private var center: PointF = PointF(250f, 750f)
    // calculate positions and sizes here, not when drawing
    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
    // make sure actual code handles padding well.
        radius = 0.3f* min(width, height).toFloat()
        center = PointF(width/2.0f, height/2.0f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> TODO()
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> TODO()
        }
        return true
    }
    private fun touchMove(x: Float, y: Float){
        TODO("Update positions and properties of drawn items")
        // will render again the screen.
        invalidate()
    }
}