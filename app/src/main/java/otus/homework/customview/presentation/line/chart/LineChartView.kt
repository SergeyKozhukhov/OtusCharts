package otus.homework.customview.presentation.line.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.parcelize.Parcelize
import otus.homework.customview.presentation.line.chart.storages.CursorStorage
import otus.homework.customview.presentation.line.chart.storages.LineAreaStorage
import otus.homework.customview.presentation.line.chart.storages.LineDataStorage
import otus.homework.customview.presentation.line.chart.storages.LinePaintStorage
import otus.homework.customview.presentation.line.chart.storages.LinePaintStorage.Companion.DEFAULT_LABEL
import kotlin.math.max

/**
 * Линейный график, позволяющий отображать положительные значения на временной оси
 */
class LineChartView constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    private val areaStorage = LineAreaStorage()
    private val dataStorage = LineDataStorage(areaStorage)
    private val cursorStorage = CursorStorage(areaStorage)
    private val paintStorage = LinePaintStorage(resources, areaStorage)

    private val lineChartPath = Path()

    /** Признак отображения отладочной информации */
    var isDebugModeEnabled: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    /** Нарисовать линйный график по данным [LineData] */
    fun render(data: LineData) {
        dataStorage.update(data)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(
            TAG, "onMeasure:" +
                    "widthMeasureSpec = ${MeasureSpec.toString(widthMeasureSpec)}, " +
                    "heightMeasureSpec = ${MeasureSpec.toString(heightMeasureSpec)}"
        )

        val label = DEFAULT_LABEL
        paintStorage.label.getTextBounds(label, 0, label.length, paintStorage.labelRect)

        val viewMinWith = paintStorage.labelRect.width() + paddingLeft + paddingRight
        val viewMinHeight = paintStorage.labelRect.height() + paddingTop + paddingBottom
        val requestedWidth = max(viewMinWith, suggestedMinimumWidth)
        val requestedHeight = max(viewMinHeight, suggestedMinimumHeight)

        val requestedSize = max(requestedWidth, requestedHeight)

        val realWidth = resolveSizeAndState(requestedSize, widthMeasureSpec, 0)
        val realHeight = resolveSizeAndState(requestedSize, heightMeasureSpec, 0)

        setMeasuredDimension(realWidth, realHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged: w = $w, h = $h, oldw = $oldw, oldh = $oldh")
        areaStorage.update(
            width = w,
            height = h,
            leftPadding = paddingLeft,
            topPadding = paddingTop,
            rightPadding = paddingRight,
            bottomPadding = paddingBottom
        )

        dataStorage.reupdate()
        paintStorage.recalculate()
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (cursorStorage.update(event.x, event.y)) invalidate()
                true
            }

            MotionEvent.ACTION_MOVE -> {
                if (cursorStorage.update(event.x, event.y)) invalidate()
                true
            }

            MotionEvent.ACTION_UP -> {
                cursorStorage.clear()
                invalidate()
                true
            }

            MotionEvent.ACTION_CANCEL -> {
                cursorStorage.clear()
                invalidate()
                false
            }

            else -> false
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isDebugModeEnabled) {
            drawDebugAreas(canvas)
            drawDebugGrid(canvas)
        }

        drawLines(canvas)
        drawCursor(canvas)
        drawLabel(canvas)
    }


    /** Нарисовать отладочную информацию по областям графика */
    private fun drawDebugAreas(canvas: Canvas) {
        canvas.drawRect(areaStorage.global, paintStorage.global)
        canvas.drawRect(areaStorage.padding, paintStorage.padding)
        canvas.drawRect(areaStorage.chart, paintStorage.chart)
    }

    /** Нарисовать отладочную информацию по "сетке" */
    private fun drawDebugGrid(canvas: Canvas) {
        val cellCount = DEBUG_CELL_COUNT
        val area = areaStorage.chart
        val stepX = area.width() / cellCount
        val stepY = area.height() / cellCount
        var currentPointX = area.left
        var currentPointY = area.top
        for (i in 0 until cellCount) {
            canvas.drawLine(
                currentPointX,
                area.bottom,
                currentPointX,
                area.top,
                paintStorage.debugGrid
            )

            canvas.drawLine(
                area.left,
                currentPointY,
                area.right,
                currentPointY,
                paintStorage.debugGrid
            )
            currentPointX += stepX
            currentPointY += stepY
        }
    }

    /** Нарисовать линии графика с градиентом */
    private fun drawLines(canvas: Canvas) {
        dataStorage.getNodes().takeIf { it.isNotEmpty() }?.let { nodes ->
            lineChartPath.reset()

            // линия
            val firstNode = nodes.first()
            lineChartPath.moveTo(firstNode.x, firstNode.y)
            nodes.forEach { node -> lineChartPath.lineTo(node.x, node.y) }
            canvas.drawPath(lineChartPath, paintStorage.line)

            // градиент
            val area = areaStorage.chart
            lineChartPath.lineTo(nodes.last().x, area.bottom)
            lineChartPath.lineTo(area.left, area.bottom)
            lineChartPath.close()
            canvas.drawPath(lineChartPath, paintStorage.gradient)
        }
    }

    /** Нарисовать вертикальную линию, соответствующую позиции курсора */
    private fun drawCursor(canvas: Canvas) {
        cursorStorage.getPoint()?.let { point ->
            canvas.drawLine(
                point.x,
                areaStorage.chart.top,
                point.x,
                areaStorage.chart.bottom,
                paintStorage.cursor
            )
        }
    }

    /** Нарисовать подпись, соответствующую позиции курсора */
    private fun drawLabel(canvas: Canvas) {
        val area = areaStorage.chart
        val cursorPoint = cursorStorage.getPoint() ?: return
        val node = dataStorage.getNodeByX(cursorPoint.x)
        val label = node?.label ?: return
        paintStorage.label.getTextBounds(label, 0, label.length, paintStorage.labelRect)
        canvas.drawText(
            label,
            area.centerX() - paintStorage.labelRect.width() / 2,
            area.bottom - paintStorage.labelRect.height() / 2,
            paintStorage.label
        )
    }

    override fun onSaveInstanceState(): Parcelable =
        SavedState(
            super.onSaveInstanceState(),
            dataStorage.origin,
            isDebugModeEnabled
        )

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        val savedState = state as SavedState
        isDebugModeEnabled = savedState.isDebugModeEnabled
        render(savedState.pieData)
    }

    @Parcelize
    class SavedState(
        private val superSavedState: Parcelable?,
        val pieData: LineData,
        val isDebugModeEnabled: Boolean,
    ) : BaseSavedState(superSavedState), Parcelable

    private companion object {

        private const val TAG = "LineChartView"

        /** Кол-во клеток "сетки" отладочной информации */
        const val DEBUG_CELL_COUNT = 10
    }
}