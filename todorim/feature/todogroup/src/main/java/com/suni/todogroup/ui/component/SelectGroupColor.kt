package com.suni.todogroup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
                AnimTextFieldDecoratorBox(
                    strTitle = strTitle.value,
                    isTextFieldFocused = isTextFieldFocused,
                    innerTextField = innerTextField,
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
