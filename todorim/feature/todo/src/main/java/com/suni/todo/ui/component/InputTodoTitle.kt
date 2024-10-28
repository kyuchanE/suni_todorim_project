package com.suni.todo.ui.component

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

/**
 * 할 일 이름 설정
 */
@Composable
fun InputTodoTitle(
    modifier: Modifier,
    strTitle: String,
    titleChangeEvent: (title: String) -> Unit = { _ -> },
) {
    TextField(
        modifier = modifier,
        value = strTitle,
        onValueChange = { titleChangeEvent(it) }
    )
}