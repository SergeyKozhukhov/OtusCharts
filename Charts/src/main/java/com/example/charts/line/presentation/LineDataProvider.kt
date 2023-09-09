package com.example.charts.line.presentation

import android.graphics.Color
import com.example.charts.line.models.LineNode
import com.example.charts.models.Chart
import com.example.charts.models.DataFrameLegacy
import java.util.Date
import kotlin.random.Random

class LineDataProvider(private val areaProvider: LineAreaProvider) {

    private val random = Random

    private val nodes = mutableListOf<LineNode>()

    var currentLineX = DEFAULT_LINE_X

    fun getCurrentLineX() = currentLineX.takeIf { it != DEFAULT_LINE_X }

    fun updateCurrentLineX(x: Float, y: Float): Boolean = if (areaProvider.local.contains(x, y)) {
        currentLineX = x
        true
    } else {
        false
    }

    fun clearCurrentLineX() {
        currentLineX = DEFAULT_LINE_X
    }

    fun calculate(dataFrame: DataFrameLegacy) {
        // кол-во
        val amountField = dataFrame.fields.first { it.type == "int" }
        val amountValues: List<Int> = amountField.values.map { it as Int }

        // временные метки
        val timeField = dataFrame.fields.first { it.type == "long" }
        val timeValues: List<Long> = timeField.values.map { it as Long }

        // нейминги категорий
        val categoryField = dataFrame.fields.first { it.type == "string" }
        val categoryValues = categoryField.values.map { it as String }

        // высота
        val baseHeight = areaProvider.local.height()

        // шаг по Y: высоту делим на сумму
        val scaleY =
            areaProvider.local.height() / (amountValues.maxOfOrNull { it }?.toFloat() ?: 1f)

        val maxAfterZero = amountValues.maxOfOrNull { it }?.toFloat() ?: 1f
        val minBeforeZero = amountValues.minOfOrNull { it }?.toFloat() ?: -1f

        // TODO: ADD ==
        val countYAfterZero = amountValues.count { it > 0 }
        val countYBeforeZero = amountValues.count { it < 0 }

        val scaleaYafterZero = maxAfterZero / countYAfterZero
        val scaleaYbeforeZero = maxAfterZero / countYBeforeZero

        val timelineMinOfX = timeValues.minOfOrNull { it } ?: 1L
        val timelineMaxOfX = timeValues.maxOfOrNull { it } ?: 1L
        // TODO: may be 0
        val timelineSpaceOfX = timelineMaxOfX - timelineMinOfX
        val scaleX = areaProvider.local.width() / (timelineSpaceOfX)

        val newValues = mutableListOf<LineNode>()

        val zero = areaProvider.local.top + maxAfterZero

        for (i in amountValues.indices) {
            val newX = areaProvider.local.left + (timeValues[i] - timelineMinOfX) * scaleX
            // val newY = areaProvider.local.top + baseHeight - (amountValues[i] * scaleY)
            val yScale = if (amountValues[i] > 0) scaleaYafterZero else scaleaYbeforeZero
            val newY = zero - (amountValues[i] * yScale)
            newValues.add(
                LineNode(
                    x = newX,
                    y = newY,
                    label = categoryValues[i],
                    color = Color.argb(
                        255, random.nextInt(256), random.nextInt(256), random.nextInt(256)
                    ),
                    date = Date(timeValues[i])
                )
            )
        }

        nodes.clear()
        nodes.addAll(newValues.sortedBy { it.x })
    }

    fun calculatePrevious(dataFrame: DataFrameLegacy) {
        // кол-во
        val amountField = dataFrame.fields.first { it.type == "int" }
        val amountValues: List<Int> = amountField.values.map { it as Int }

        // временные метки
        val timeField = dataFrame.fields.first { it.type == "long" }
        val timeValues: List<Long> = timeField.values.map { it as Long }

        // нейминги категорий
        val categoryField = dataFrame.fields.first { it.type == "string" }
        val categoryValues = categoryField.values.map { it as String }

        // высота
        val baseHeight = areaProvider.local.height()

        // шаг по Y: высоту делим на сумму
        val scaleY =
            areaProvider.local.height() / (amountValues.maxOfOrNull { it }?.toFloat() ?: 1f)

        val timelineMinOfX = timeValues.minOfOrNull { it } ?: 1L
        val timelineMaxOfX = timeValues.maxOfOrNull { it } ?: 1L
        // TODO: may be 0
        val timelineSpaceOfX = timelineMaxOfX - timelineMinOfX
        val scaleX = areaProvider.local.width() / (timelineSpaceOfX)

        val newValues = mutableListOf<LineNode>()


        for (i in amountValues.indices) {
            val newX = areaProvider.local.left + (timeValues[i] - timelineMinOfX) * scaleX
            val newY = areaProvider.local.top + baseHeight - (amountValues[i] * scaleY)
            newValues.add(
                LineNode(
                    x = newX,
                    y = newY,
                    label = categoryValues[i],
                    color = Color.argb(
                        255, random.nextInt(256), random.nextInt(256), random.nextInt(256)
                    ),
                    date = Date(timeValues[i])
                )
            )
        }

        nodes.clear()
        nodes.addAll(newValues.sortedBy { it.x })
    }

    fun calculate(chart: Chart) {
        val baseWidth = areaProvider.local.width()
        val baseHeight = areaProvider.local.height()
        val step = baseWidth / (chart.nodes.size - 1)

        val scaleY =
            areaProvider.local.height() / (chart.nodes.maxOfOrNull { it.amount }?.toFloat() ?: 1f)

        val timelineMinOfX = chart.nodes.minOfOrNull { it.time } ?: 1L
        val timelineMaxOfX = chart.nodes.maxOfOrNull { it.time } ?: 1L
        // TODO: may be 0
        val timelineSpaceOfX = timelineMaxOfX - timelineMinOfX
        val scaleX = areaProvider.local.width() / (timelineSpaceOfX)

        val newValues = mutableListOf<LineNode>()


        chart.nodes.forEachIndexed { index, chartNode ->
            val newX = areaProvider.local.left + (chartNode.time - timelineMinOfX) * scaleX
            val newY = areaProvider.local.top + baseHeight - (chartNode.amount * scaleY)
            newValues.add(
                LineNode(
                    x = newX,
                    y = newY,
                    label = chartNode.category,
                    color = Color.argb(
                        255, random.nextInt(256), random.nextInt(256), random.nextInt(256)
                    ),
                    date = Date(chartNode.time)
                )
            )
        }

        nodes.clear()
        nodes.addAll(newValues.sortedBy { it.x })
    }

    fun getNodeByX(x: Float) = nodes.findLast { it.x < x }
    fun getCurrentNode() = nodes.findLast { it.x < currentLineX }

    fun getNodes(): List<LineNode> = nodes

    private companion object {
        const val DEFAULT_LINE_X = -1f
    }
}
