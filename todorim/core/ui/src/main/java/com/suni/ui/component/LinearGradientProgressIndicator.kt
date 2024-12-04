package com.suni.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 그라데이션 프로그레스 바
 * @param modifier: Modifier
 * @param backgroundColor: Color : 배경 색상
 * @param colorIndex: Int : 색상 인덱스
 * @param percent: Float : 진행률
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
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val boxModifier = Modifier.clip(RoundedCornerShape(10.dp))
        BoxWithConstraints(
            modifier = boxModifier
                .weight(1f)
                .background(backgroundColor)
        ) {
            Box(
                modifier = boxModifier
                    .animateContentSize()
                    .width(maxWidth * percent / 100)
                    .height(30.dp)
                    .background(bgGradient(bgIndex = colorIndex, isVertical = false))
            )
        }
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            text = "${percent.toInt()} %",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 9.sp,
            overflow = TextOverflow.Ellipsis,
        )
    }
}