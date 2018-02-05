package com.em.hcpractice1.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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
class CircleView(context: Context) : View(context) {
    private val normalPaint: Paint = Paint()
    private val eoPaint: Paint = Paint()
    private val windingPaint: Paint = Paint()
    private val eoPath: Path = Path()
    private val windingPath: Path = Path()

    init {
        normalPaint.run {
            color = Color.parseColor("#56abe3")
            isAntiAlias = true
        }
        eoPaint.run {
            color = Color.parseColor("#56abe3")
            isAntiAlias = true
        }
        windingPaint.run {
            color = Color.parseColor("#56abe3")
            isAntiAlias = true
        }
        eoPath.run {
            addCircle(520f, 180f, 100f, Path.Direction.CW)
            addCircle(520f, 180f, 170f, Path.Direction.CCW)
            fillType = Path.FillType.EVEN_ODD
        }
        windingPath.run {
            addCircle(870f, 180f, 170f, Path.Direction.CCW)
            addCircle(890f, 180f, 170f, Path.Direction.CW)
            fillType = Path.FillType.WINDING
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(170f, 170f, 170f, normalPaint)
        canvas.drawPath(eoPath, eoPaint)
        canvas.drawPath(windingPath, windingPaint)
    }
}