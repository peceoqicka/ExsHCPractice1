package com.em.hcpractice1.view

import android.content.Context
import android.graphics.*
import android.view.View

/**
 * <pre>
 *      author  :   Acer
 *      e-mail  :   xx@xxx
 *      time    :   2018/1/30
 *      desc    :
 *      version :   1.0
 * </pre>
 */
class PathView(context: Context) : View(context) {
    companion object {
        const val RADIUS = 200f
        const val RADIUS_PLUS = 230f
    }

    private val startX = 300f
    private val startY = 300f
    private val paint: Paint = Paint()
    private val refPaint: Paint = Paint()
    private val path: Path = Path()
    private var rect2: RectF = RectF()
    private var p1: PointF = PointF()
    private var p2: PointF = PointF()

    init {
        val rect1 = RectF(startX - RADIUS, startY - RADIUS, startX + RADIUS, startY + RADIUS)
        rect2 = RectF(startX - RADIUS_PLUS, startY - RADIUS_PLUS, startX + RADIUS_PLUS, startY + RADIUS_PLUS)
        p1 = PointF((startX - Math.cos(60.toRadian()) * RADIUS_PLUS).toFloat(),
                (startY - Math.sin(60.toRadian()) * RADIUS_PLUS).toFloat())
        p2 = PointF((startX + Math.cos(60.toRadian()) * RADIUS).toFloat(),
                (startY - Math.sin(60.toRadian()) * RADIUS).toFloat())
        paint.run {
            color = Color.parseColor("#56abe3")
            style = Paint.Style.STROKE
            strokeWidth = 5f
            textSize = 50f
        }
        refPaint.run {
            color = Color.parseColor("#FF0000")
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
        path.run {
            addArc(rect1, 180f, 60f)
            lineTo(p1.x, p1.y)
            addArc(rect2, 240f, 60f)
            lineTo(p2.x, p2.y)
            addArc(rect1, 300f, 60f)
            lineTo(rect1.left, rect1.centerY())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rect2, refPaint)
        canvas.drawPath(path, paint)
        canvas.drawPoint(startX, startY, refPaint)
    }

    private fun Int.toRadian() = (this * Math.PI) / 180
}