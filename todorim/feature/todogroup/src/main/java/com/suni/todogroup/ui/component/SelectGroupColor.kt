package com.suni.todogroup.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.suni.ui.component.ColorPalette

/**
 * 그룹 색상 선택 및 타이틀
 * @param modifier
 */
@Composable
fun SelectGroupColor(
    modifier: Modifier,
    strTitle: MutableState<String>,
    titleChangeEvent: (title:String) -> Unit = { _ -> },
    selectedColorIndex: Int = 0,
    selectedColorEvent: (colorIndex:Int) -> Unit = { _ -> },
) {
    val textFieldFocusRequest = remember { FocusRequester() }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        BasicTextField(
            value = strTitle.value,
            onValueChange = {
                titleChangeEvent(it)
            },
            modifier = Modifier
                .focusRequester(textFieldFocusRequest)
                .onFocusChanged { focusState ->
                    isTextFieldFocused = focusState.isFocused
                }
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            decorationBox = { innerTextField ->
                TextFieldDecoratorBox(
                    strTitle.value,
                    isTextFieldFocused,
                    innerTextField,
                )
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        ColorPalette(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            selectedIndex = selectedColorIndex,
            selectColorEvent = selectedColorEvent
        )
    }
}

/**
 * TextField Decorator Box - 그룹 이름 입력 UI
 * @param strTitle TextField Value
 * @param isTextFieldFocused TextField Focus
 * @param innerTextField TextField
 */
@Composable
private fun TextFieldDecoratorBox(
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
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
            ) {
                innerTextField()
            }

            // TextField Border Animation
//            androidx.compose.animation.AnimatedContent(
//                targetState = isVisibleBorder,
//                transitionSpec = {
//                    fadeIn() togetherWith fadeOut()
//                },
//                label = "TextField Border Animation",
//            ) { isVisible ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .background(
//                            color = Color.Transparent,
//                            shape = RectangleShape,
//                        )
//                        .border(
//                            border = BorderStroke(
//                                1.5.dp,
//                                colorResource(
//                                    id =
//                                    if (isVisible) com.suni.ui.R.color.tdr_default
//                                    else com.suni.ui.R.color.transparent
//                                )
//                            ),
//                            shape = RectangleShape,
//                        ),
//                )
//            }

            androidx.compose.animation.AnimatedVisibility(
                visible = isVisibleBorder,
                enter =
                slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                    // Offsets the content by 1/3 of its width to the left, and slide towards right
                    // Overwrites the default animation with tween for this slide animation.
                    -fullWidth / 3
                } +
                        fadeIn(
                            // Overwrites the default animation with tween
                            animationSpec = tween(durationMillis = 200)
                        ),
                exit =
                slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                    // Overwrites the ending position of the slide-out to 200 (pixels) to the right
                    200
                } + fadeOut()
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
            }

            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.5.dp),
                color = colorResource(com.suni.ui.R.color.tdr_default),
            )
        }
        // TextField Hint Animation
        androidx.compose.animation.AnimatedContent(
            targetState = isVisibleBorder,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
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