package com.suni.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource

/**
 * 그라데이션 원(수평)
 * @param startColor
 * @param endColor
 * @param modifier
 * @param clickEvent
 */
@Composable
fun CircleGradientHorizontal(
    startColor: Int,
    endColor: Int,
    modifier: Modifier,
    clickEvent: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        colorResource(id = startColor),
                        colorResource(id = endColor),
                    )
                ),
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember{ MutableInteractionSource() },
                indication = null
            ) {
                clickEvent()
            }
    )
}