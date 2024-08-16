package com.example.jetpackcomposechatapp.uiComponents.shape

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

internal class HoleRectShape(
    private val holeSizePx: Float,
    private val holesCount: Int,
    private val holeIndex: Int,
    @FloatRange(.0, 1.0) private val holeProgress: Float,
    private val previousHoleIndex: Int,
    @FloatRange(.0, 1.0) private val previousHoleProgress: Float,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(
        getHoleRectPath(
            size = size,
            holeSizePx = holeSizePx,
            holesCount = holesCount,
            holeIndex = holeIndex,
            holeProgress = holeProgress,
            previousHoleIndex = previousHoleIndex,
            previousHoleProgress = previousHoleProgress
        )
    )
}

internal fun getHoleRectPath(
    size: Size,
    holeSizePx: Float,
    holesCount: Int,
    holeIndex: Int,
    @FloatRange(.0, 1.0) holeProgress: Float,
    previousHoleIndex: Int,
    @FloatRange(.0, 1.0) previousHoleProgress: Float,
) = if (holeIndex >= holesCount) {
    throw IllegalArgumentException("holeSpot $holeIndex must be minor that stops: $holesCount")
} else Path().apply {
    moveTo(0f, 0f)

    //here is where the fun begins
    repeat(holesCount) { index ->
        val currentWidth = size.width * index / holesCount
        val nextWidthStep = size.width * (index + 1) / holesCount
        val middleXPoint = (currentWidth + nextWidthStep) / 2

        when (index) {
            holeIndex -> {
                getSingleHolePath(
                    holeSizePx = holeSizePx, centerX = middleXPoint, deepProgress = holeProgress
                )
            }

            previousHoleIndex -> {
                getSingleHolePath(
                    holeSizePx = holeSizePx,
                    centerX = middleXPoint,
                    deepProgress = previousHoleProgress
                )
            }

            else -> {
                if (index == holesCount - 1) {
                    lineTo(size.width, 0f)
                } else {
                    val paddingPx = holeSizePx.times(.2f)
                    val twoStepsAhead = size.width * (index + 2) / holesCount
                    val nextXPoint = (nextWidthStep + twoStepsAhead) / 2 - (holeSizePx + paddingPx)
                    lineTo(nextXPoint, 0f)
                }
            }
        }
    }

    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    lineTo(0f, 0f)
}

internal fun Path.getSingleHolePath(
    holeSizePx: Float,
    centerX: Float,
    @FloatRange(.0, 1.0) deepProgress: Float,
) {
    val paddingPx = holeSizePx.times(.2f)

    moveTo(x = centerX - (holeSizePx + paddingPx), y = 0f)
    cubicTo(
        x3 = centerX - paddingPx,
        y3 = (holeSizePx / 2 + paddingPx.times(.8f)) * deepProgress,
        x1 = centerX - holeSizePx.times(.33f) - paddingPx,
        y1 = holeSizePx.times(.1f) * deepProgress,
        x2 = centerX - holeSizePx.times(.66f) - paddingPx,
        y2 = holeSizePx * 2 / 4 * deepProgress,
    )

    quadraticBezierTo(
        x2 = centerX + paddingPx,
        y2 = (holeSizePx / 2 + paddingPx.times(.8f)) * deepProgress,
        x1 = centerX,
        y1 = (holeSizePx / 2 + paddingPx) * deepProgress
    )

    cubicTo(
        x3 = centerX + holeSizePx + paddingPx,
        y3 = 0f,
        x2 = centerX + holeSizePx.times(.33f) + paddingPx,
        y2 = holeSizePx.times(.1f) * deepProgress,
        x1 = centerX + holeSizePx.times(.66f) + paddingPx,
        y1 = holeSizePx / 2 * deepProgress,
    )
}