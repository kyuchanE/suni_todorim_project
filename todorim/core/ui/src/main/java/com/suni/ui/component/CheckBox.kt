package com.suni.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.suni.ui.R

/**
 * 체크박스 (최상위 Row clickable)
 * @param modifier
 * @param todoTitle
 * @param isSelected 체크박스의 선택 여부
 * @param onCheckedAction
 */
@Composable
fun TdrCheckBox(
    modifier: Modifier,
    todoTitle: String,
    isSelected: Boolean = false,
    onCheckedAction: (isSelected: Boolean) -> Unit = { _ -> },
) {
    val checkBoxState = remember { mutableStateOf(isSelected) }

    Card(
        modifier = modifier,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.tdr_default),
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.DarkGray,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    checkBoxState.value = !checkBoxState.value
                    onCheckedAction(checkBoxState.value)
                }
        ) {
            val boxModifier = Modifier.size(24.dp)
            if (checkBoxState.value) {
                CheckBoxSelected(
                    modifier = boxModifier
                )
            } else {
                CheckBoxUnSelected(
                    modifier = boxModifier
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = todoTitle,
                textDecoration =
                if (checkBoxState.value) TextDecoration.LineThrough
                else TextDecoration.None,
            )
        }
    }
}

/**
 * 체크박스 (Only 체크박스 clickable)
 * @param modifier
 * @param todoTitle
 * @param isSelected 체크박스의 선택 여부
 * @param onCheckedAction
 */
@Composable
fun TdrOnlyCheckBox(
    modifier: Modifier,
    todoTitle: String,
    isSelected: Boolean = false,
    onCheckedAction: (isSelected: Boolean) -> Unit = { _ -> },
) {
    val checkBoxState = remember { mutableStateOf(isSelected) }

    Card(
        modifier = modifier,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.tdr_default),
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.DarkGray,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            val boxModifier = Modifier
                .size(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    checkBoxState.value = !checkBoxState.value
                    onCheckedAction(checkBoxState.value)
                }

            if (checkBoxState.value) {
                CheckBoxSelected(
                    modifier = boxModifier
                )
            } else {
                CheckBoxUnSelected(
                    modifier = boxModifier
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = todoTitle,
                textDecoration =
                if (checkBoxState.value) TextDecoration.LineThrough
                else TextDecoration.None,
            )
        }
    }
}

@Composable
private fun CheckBoxSelected(
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_gray),
            tint = Color.LightGray,
            contentDescription = "Checked",
        )
    }
}

@Composable
private fun CheckBoxUnSelected(
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_uncheck_default),
            contentDescription = "UnChecked",
        )
    }
}