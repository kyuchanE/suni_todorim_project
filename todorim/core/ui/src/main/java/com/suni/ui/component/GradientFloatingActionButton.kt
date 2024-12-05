package com.suni.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
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
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(id = startColor),
                        colorResource(id = endColor),
                    )
                ),
                shape = CircleShape,
            )
            .size(65.dp),
        onClick = onClickAction,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            contentDescription = "Gradient FAB",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GradientFloatingActionButton(
    modifier: Modifier,
    startColor: Int,
    endColor: Int,
    icon: ImageVector,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    keySharedElement: String,
    onClickAction: () -> Unit = {},
) {
    with(sharedTransitionScope) {
        IconButton(
            modifier = modifier
                .sharedBounds(
                    rememberSharedContentState(keySharedElement),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colorResource(id = startColor),
                            colorResource(id = endColor),
                        )
                    ),
                    shape = CircleShape,
                )
                .size(65.dp),
            onClick = onClickAction,
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = "Gradient FAB",
                tint = Color.White
            )
        }
    }
}
