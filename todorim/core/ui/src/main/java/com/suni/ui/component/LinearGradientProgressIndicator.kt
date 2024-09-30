package com.suni.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 그라데이션 프로그레스 바
 */
@Composable
fun LinearGradientProgressIndicator(
    modifier: Modifier,
    backgroundColor: Color,
    colorIndex: Int = 0,
    percent: Float = 0f,
) {
    Row(
        modifier = modifier,
    ) {
        val boxModifier = Modifier.clip(RoundedCornerShape(10.dp))
        BoxWithConstraints(
            modifier = boxModifier
                .weight(1f)
                .background(backgroundColor)
        ) {
            Box(
                modifier = boxModifier
                    .width(maxWidth * percent / 100)
                    .height(30.dp)
                    .background(bgGradient(bgIndex = colorIndex, isVertical = false))
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        //
        Text(text = "${percent.toInt()} %")
    }
}