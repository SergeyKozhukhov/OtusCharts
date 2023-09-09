package com.example.charts.line.presentation

import android.graphics.Rect
import android.graphics.RectF
import com.example.charts.line.presentation.LinePaints

class LineAreaProvider(private val paints: LinePaints) {

    val global = Rect()
    val padding = Rect()
    val local = RectF()

    fun update(
        leftPosition: Int,
        topPosition: Int,
        rightPosition: Int,
        bottomPosition: Int,
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        global.set(leftPosition, topPosition, rightPosition, bottomPosition)

        /* size with paddings */
        padding.set(
            global.left + leftPadding,
            global.top + topPadding,
            global.right - rightPadding,
            global.bottom - bottomPadding
        )
        /*        val widthR = global.right - rightPadding - global.left + leftPadding
                val heightR = global.bottom - bottomPadding - global.top + topPadding*/

        local.set(padding)

        /*val localRadius = minOf(padding.width(), padding.height()) / 2f

        val centerX = padding.exactCenterX()
        val centerY = padding.exactCenterY()

        local.set(*//* left = *//* centerX - localRadius,*//* top = *//*
            centerY - localRadius,*//* right = *//*
            centerX + localRadius,*//* bottom = *//*
            centerY + localRadius
        )*/
    }
}
/*

global.set(leftPosition, topPosition, rightPosition, bottomPosition)

*/
/* size with paddings *//*

padding.set(
global.left + leftPadding,
global.top + topPadding,
global.right - rightPadding,
global.bottom - bottomPadding
)
*/
/*        val widthR = global.right - rightPadding - global.left + leftPadding
        val heightR = global.bottom - bottomPadding - global.top + topPadding*//*


val localRadius = minOf(padding.width(), padding.height()) / 2f

val centerX = padding.exactCenterX()
val centerY = padding.exactCenterY()

local.set(*/
/* left = *//*
 centerX - localRadius,*/
/* top = *//*

centerY - localRadius,*/
/* right = *//*

centerX + localRadius,*/
/* bottom = *//*

centerY + localRadius
)*/
