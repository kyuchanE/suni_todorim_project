package com.suni.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import com.suni.ui.R

/**
 * 그라데이션 FAB
 */
@Composable
fun GradientFloatingActionButton(
    modifier: Modifier,
    startColor: Int,
    endColor: Int,
    icon: ImageVector,
    onClickAction: () -> Unit = {},
) {
    IconButton(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                listOf(
                    colorResource(id = startColor),
                    colorResource(id = endColor),
                )
            ),
            shape = CircleShape,
        ),
        onClick = onClickAction,
    ) {
//        Icon(painter = icon, contentDescription = "Gradient FAB", tint = Color.White)
        Icon(imageVector = icon, contentDescription = "Gradient FAB", tint = Color.White)
    }
}
