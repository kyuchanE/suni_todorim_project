package com.suni.todo.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.suni.ui.component.AnimTextFieldDecoratorBox

/**
 * 할 일 이름 설정
 */
@Composable
fun InputTodoTitle(
    modifier: Modifier,
    strTitle: String,
    titleChangeEvent: (title: String) -> Unit = { _ -> },
) {
    val textFieldFocusRequest = remember { FocusRequester() }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .focusRequester(textFieldFocusRequest)
            .onFocusChanged { focusState ->
                isTextFieldFocused = focusState.isFocused
            }
            .padding(horizontal = 15.dp),
        value = strTitle,
        onValueChange = {
            titleChangeEvent(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        decorationBox = { innerTextField ->
            AnimTextFieldDecoratorBox(
                strTitle = strTitle,
                isTextFieldFocused = isTextFieldFocused,
                innerTextField = innerTextField
            )
        }
    )

}