package com.example.charts.pie

import android.graphics.Color
import com.example.charts.models.Chart
import com.example.charts.models.DataFrameLegacy
import kotlin.random.Random

class PieDataProvider {

    private val random = Random

    private var pieAngleNodes = emptyList<PieAngleNode>()

    fun calculate(chart: Chart) {
        val generalAmount = chart.nodes.sumOf { it.amount }
        var count = 0f
        val newValues = chart.nodes.map { node ->
            PieAngleNode(
                label = node.category,
                angleStart = count,
                angleSweep = node.amount.toFloat() / (generalAmount.toFloat()) * 360f,
                color = Color.argb(
                    255,
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                )
            ).also { angleNode ->
                count += angleNode.angleSweep
            }
        }
        pieAngleNodes = newValues.sortedBy { it.angleStart }
    }

    fun calculate(dataFrame: DataFrameLegacy) {
        val amountField = dataFrame.fields.first { it.type == "int" }
        val amountValues = amountField.values.map { it as Int }
        val generalAmount = amountValues.sumOf { it }

        val categoryField = dataFrame.fields.first { it.type == "string" }
        val categoryValues = categoryField.values.map { it as String }

        var count = 0f
        val newValues2 = amountValues.zip(categoryValues) { amount, category ->
            PieAngleNode(
                label = category,
                angleStart = count,
                angleSweep = amount.toFloat() / (generalAmount.toFloat()) * 360f,
                color = Color.argb(
                    255,
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                )
            ).also { angleNode ->
                count += angleNode.angleSweep
            }
        }

        pieAngleNodes = newValues2.sortedBy { it.angleStart }
    }


    fun getNodes(): List<PieAngleNode> = pieAngleNodes

    fun getCategory(angle: Float) =
        pieAngleNodes.find { it.angleStart <= angle && it.angleStart + it.angleSweep > angle }?.label


    fun getNode(angle: Float) =
        pieAngleNodes.find { it.angleStart <= angle && it.angleStart + it.angleSweep > angle }
}