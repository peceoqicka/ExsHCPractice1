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
class HistogramView(context: Context) : View(context) {
    companion object {
        const val SPACE = 27f
        const val STEP_HEIGHT = 7f
        const val TOP_OFFSET = 20f
        const val LEFT_OFFSET = 50f
        const val LABEL_OFFSET = 48f
    }

    var adapter: Adapter? = null
        set(value) {
            field = value;setAdapter()
        }
    private var labelWidth = 90f
    private var maxRectHeight = 200f
    private var axisPath: Path = Path()
    private val blockPaint: Paint = Paint()
    private val axisPaint: Paint = Paint()
    private val labelPaint: Paint = Paint()
    private var blockList = ArrayList<Block>()

    init {
        blockPaint.run {
            color = Color.parseColor("#16ec39")
        }
        labelPaint.run {
            textSize = 42f
            color = Color.parseColor("#000000")
        }
        axisPaint.run {
            strokeWidth = 7f
            style = Paint.Style.STROKE
            color = Color.parseColor("#000000")
        }
    }

    private fun setAdapter() {
        adapter?.let { _adapter ->
            blockList.clear()
            labelWidth = 200f
            maxRectHeight = 200f

            (0 until _adapter.getItemCount()).mapTo(blockList, { position ->
                val width = labelPaint.measureText(_adapter.getName(position))
                if (labelWidth < width) {
                    labelWidth = width
                }
                val height = _adapter.getValue(position) * STEP_HEIGHT * 100
                if (height > maxRectHeight) {
                    maxRectHeight = height
                }

                Block(_adapter.getName(position), _adapter.getValue(position),
                        RectF().apply {
                            left = SPACE + if (position > 0) blockList[position - 1].rect.right else LEFT_OFFSET
                            top = TOP_OFFSET + maxRectHeight - height
                            right = left + width
                            bottom = top + height
                        })
            })

            axisPath = Path()
            axisPath.run {
                moveTo(LEFT_OFFSET, 0f)
                lineTo(LEFT_OFFSET, maxRectHeight + TOP_OFFSET)
                lineTo(blockList.last().rect.right + SPACE, maxRectHeight + TOP_OFFSET)
            }

            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.run {
            blockList.forEach { b ->
                //draw graph
                drawRect(b.rect, blockPaint)
                //draw label
                drawText(b.name, b.rect.left, b.rect.bottom + LABEL_OFFSET, labelPaint)
            }
            //draw axis
            drawPath(axisPath, axisPaint)
        }
    }

    private data class Block(var name: String, var value: Float, var rect: RectF)

    abstract class Adapter {
        abstract fun getItemCount(): Int
        abstract fun getName(position: Int): String
        abstract fun getValue(position: Int): Float
    }
}