package com.suni.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * TextField Decorator Box - 그룹 이름 입력 UI
 * @param strTitle TextField Value
 * @param isTextFieldFocused TextField Focus
 * @param innerTextField TextField
 */
@Composable
fun AnimTextFieldDecoratorBox(
    strTitle: String,
    isTextFieldFocused: Boolean,
    innerTextField : @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(75.dp)
            .background(
                color = Color.Transparent,
                shape = RectangleShape,
            ),
    ) {
        val isVisibleBorder = if (isTextFieldFocused) {
            true
        } else {
            strTitle.trim().isNotEmpty()
        }

        Box(
            modifier = Modifier
                .height(50.dp)
                .background(
                    color = Color.Transparent,
                    shape = RectangleShape,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = Color.Transparent,
                        shape = RectangleShape,
                    )
                    .border(
                        border = BorderStroke(
                            1.5.dp,
                            colorResource(
                                id = com.suni.ui.R.color.tdr_default
                            )
                        ),
                        shape = RectangleShape,
                    ),
            )

            // TextField Border Animation
            androidx.compose.animation.AnimatedVisibility(
                visible = !isVisibleBorder,
                enter =
                slideInHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    -fullWidth
                },
                exit =
                slideOutHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    -fullWidth
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.5.dp)
                        .background(
                            color = Color.White,
                            shape = RectangleShape,
                        )
                        .align(Alignment.TopStart),
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
            ) {
                innerTextField()
            }

        }
        // TextField Hint Animation
        androidx.compose.animation.AnimatedContent(
            targetState = isVisibleBorder,
            transitionSpec = {
                slideInVertically(animationSpec = tween(durationMillis = 500)) { fullHeight ->
                    +fullHeight
                } togetherWith slideOutVertically(animationSpec = tween(durationMillis = 500)) { fullHeight ->
                    +fullHeight
                }
            },
            label = "TextField Hint Animation",
        ) { isMoveBottom ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .padding( bottom =
                        if (isMoveBottom) 0.dp
                        else 10.dp,
                        )
                        .align(
                            if (isMoveBottom) Alignment.BottomStart
                            else Alignment.CenterStart
                        ),
                    text = stringResource(com.suni.ui.R.string.str_hint_group_name),
                    style =
                    if (isMoveBottom) MaterialTheme.typography.bodySmall
                    else MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                )
            }

        }

    }
}