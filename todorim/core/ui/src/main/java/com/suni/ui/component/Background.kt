package com.suni.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import com.suni.ui.R

@Composable
fun bgGradient(bgIndex:Int) : Brush {
    return Brush.verticalGradient(
        listOf(
            colorResource(id = startColor(bgIndex)),
            colorResource(id = endColor(bgIndex)),
        )
    )
}

private fun startColor(bgIndex: Int): Int {
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

private fun endColor(bgIndex: Int): Int {
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