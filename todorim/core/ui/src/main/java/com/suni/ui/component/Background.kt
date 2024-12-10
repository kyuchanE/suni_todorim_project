package com.suni.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.suni.ui.R

@Composable
fun bgGradient(
    bgIndex:Int,
    isVertical: Boolean = true,
) : Brush {
    return if (isVertical) {
        Brush.verticalGradient(
            listOf(
                colorResource(id = startColor(bgIndex)),
                colorResource(id = endColor(bgIndex)),
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                colorResource(id = startColor(bgIndex)),
                colorResource(id = endColor(bgIndex)),
            )
        )
    }
}

fun Modifier.topBorder(
    color: Color,
    height: Float,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        strokeWidth = height,
    )
}

fun Modifier.rightBorder(
    color: Color,
    width: Float,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(size.width, 0f),
        end = Offset(size.width, size.height),
        strokeWidth = width,
    )
}

fun Modifier.bottomBorder(
    color: Color,
    height: Float,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = height,
    )
}

fun Modifier.leftBorder(
    color: Color,
    width: Float,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        strokeWidth = width,
    )
}

fun startColor(bgIndex: Int): Int {
    return when(bgIndex) {
        1 -> R.color.gradient_start_1
        2 -> R.color.gradient_start_2
        3 -> R.color.gradient_start_3
        4 -> R.color.gradient_start_4
        5 -> R.color.gradient_start_5
        6 -> R.color.gradient_start_6
        7 -> R.color.gradient_start_7
        8 -> R.color.gradient_start_8
        9 -> R.color.gradient_start_9
        else -> R.color.tdr_default
    }
}

fun endColor(bgIndex: Int): Int {
    return when(bgIndex) {
        1 -> R.color.gradient_end_1
        2 -> R.color.gradient_end_2
        3 -> R.color.gradient_end_3
        4 -> R.color.gradient_end_4
        5 -> R.color.gradient_end_5
        6 -> R.color.gradient_end_6
        7 -> R.color.gradient_end_7
        8 -> R.color.gradient_end_8
        9 -> R.color.gradient_end_9
        else -> R.color.tdr_default
    }
}