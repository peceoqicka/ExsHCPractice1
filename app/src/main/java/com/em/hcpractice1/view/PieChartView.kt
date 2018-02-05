package com.em.hcpractice1.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
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
class PieChartView(context: Context) : View(context) {
    companion object {
        const val CENTER_X = 500f
        const val CENTER_Y = 500f
        const val RADIUS = 200f
        const val LABEL_X_OFFSET = 10f
        const val LABEL_LINE_FIRST = 30f
        const val LABEL_LINE_SECOND = 150f
    }

    var adapter: Adapter? = null
        set(value) {
            field = value;setAdapter()
        }

    private val paint = Paint()
    private val linePaint = Paint()
    private val labelPaint = Paint()
    private val pieList = ArrayList<Pie>()

    init {
        paint.run {
            style = Paint.Style.FILL
        }
        linePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
        labelPaint.run {
            style = Paint.Style.STROKE
            textSize = 40f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pieList.isNotEmpty()) {
            pieList.forEach { pie ->
                paint.color = pie.color
                canvas.drawPath(pie.path, paint)
                canvas.drawPath(pie.linePath, linePaint)
                val labelPoint = pie.labelPoint
                val textWidth = labelPaint.measureText(pie.name)
                val textX = labelPoint.startPoint.x + if (labelPoint.isEast) LABEL_X_OFFSET else -(textWidth + LABEL_X_OFFSET)
                canvas.drawText(pie.name, textX, labelPoint.startPoint.y, labelPaint)
            }
        }
    }

    private fun setAdapter() {
        adapter?.let { _adapter ->
            pieList.clear()
            var startAngle0 = 0f
            var sweepAngle0: Float
            (0 until _adapter.getItemCount()).mapTo(pieList, { position ->
                val value = _adapter.getValue(position)
                sweepAngle0 = 360 * value
                val halfAngle = startAngle0 + sweepAngle0 / 2
                val tangentX = (CENTER_X + Math.cos(halfAngle.degreeToRadian()) * RADIUS).toFloat()
                val tangentY = (CENTER_Y + Math.sin(halfAngle.degreeToRadian()) * RADIUS).toFloat()
                val labelLinePath = createLabelLine(tangentX, tangentY, CENTER_X, CENTER_Y)
                Pie(_adapter.getName(position), _adapter.getValue(position),
                        _adapter.getColor(position),
                        createPiePath(CENTER_X, CENTER_Y, RADIUS, startAngle0, sweepAngle0),
                        labelLinePath.path, labelLinePath.endPoint
                ).apply { startAngle0 += sweepAngle0 }
            })
            postInvalidate()
        }
    }

    private fun Float.degreeToRadian() = (Math.PI * this) / 180

    private fun createPiePath(centerX: Float, centerY: Float, radius: Float, startAngle: Float, sweepAngle: Float) =
            Path().apply {
                moveTo(centerX, centerY)
                arcTo(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, sweepAngle, false)
                close()
            }

    private fun createLabelLine(startX: Float, startY: Float, centerX: Float, centerY: Float): LabelLinePath {
        val isEast = (startX > centerX)
        val isSouth = (startY > centerY)
        val delta1 = (LABEL_LINE_FIRST * Math.sin(45 * Math.PI / 180)).toFloat()
        val endPoint = PointF(LABEL_LINE_SECOND * (if (isEast) 1 else -1) + startX, delta1 * (if (isSouth) 1 else -1) + startY)
        return LabelLinePath(Path().apply {
            moveTo(startX, startY)
            lineTo(delta1 * (if (isEast) 1 else -1) + startX,
                    delta1 * (if (isSouth) 1 else -1) + startY)
            lineTo(endPoint.x, endPoint.y)
        }, LabelPoint(endPoint, isEast))
    }

    private data class LabelPoint(var startPoint: PointF, var isEast: Boolean)
    private data class LabelLinePath(var path: Path, var endPoint: LabelPoint)

    private data class Pie(var name: String, var value: Float, var color: Int,
                           var path: Path, var linePath: Path, var labelPoint: LabelPoint)

    abstract class Adapter {
        abstract fun getItemCount(): Int
        abstract fun getName(position: Int): String
        abstract fun getValue(position: Int): Float
        abstract fun getColor(position: Int): Int
    }
}