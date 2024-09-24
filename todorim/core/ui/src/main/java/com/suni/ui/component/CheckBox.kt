package com.suni.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.suni.ui.R

/**
 * 체크박스
 * @param modifier
 * @param todoId
 * @param todoTitle
 * @param isSelected
 * @param onCheckedAction
 */
@Composable
fun TdrCheckBox(
    modifier: Modifier,
    todoId: Int,
    todoTitle: String,
    isSelected: Boolean = false,
    onCheckedAction: (todoId: Int) -> Unit = { _ -> },
) {
    var checkBoxState = remember { mutableStateOf(isSelected) }

    Card(
        modifier = modifier,
        onClick = {
            checkBoxState.value = !checkBoxState.value
            onCheckedAction(todoId)
                  },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (checkBoxState.value) CheckBoxSelected()
            else CheckBoxUnSelected()
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
private fun CheckBoxSelected() {
    Box(modifier = Modifier.size(24.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.check_gray),
            contentDescription = "Checked",
        )
    }
}

@Composable
private fun CheckBoxUnSelected() {
    Box(modifier = Modifier.size(24.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.uncheck_default),
            contentDescription = "UnChecked",
        )
    }
}