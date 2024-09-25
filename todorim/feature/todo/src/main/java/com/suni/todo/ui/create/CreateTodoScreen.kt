package com.suni.todo.ui.create

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.data.model.GroupEntity
import com.suni.domain.findActivity
import com.suni.todo.R
import com.suni.ui.component.GradientButton

@Composable
fun CreateTodoScreen(
    viewModel: CreateTodoScreenViewModel,
    groupId: Int,
    groupColorIndex: Int,
    todoMaxId: Int,
    finishActivityAction: () -> Unit = {},
) {
    val context = LocalContext.current
    val isFinished = viewModel.state.isFinished
    val currentFocus = LocalFocusManager.current
    val strTodoTitle = remember { mutableStateOf("") }

    LaunchedEffect(isFinished) {
        if (isFinished)
            finishActivityAction()
    }

    Scaffold { pv ->
        Surface(
            modifier = Modifier
                .padding(pv)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        currentFocus.clearFocus()
                    }
            ) {
                // 최상단 타이틀
                CreateTodoTitle(context = context, modifier = Modifier.fillMaxWidth())
                // 할 일 정보 기입
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    // 할 일 이름
                    InputTodoTitle(
                        modifier = Modifier.fillMaxWidth(),
                        strTitle = strTodoTitle,
                    ) { title ->
                        strTodoTitle.value = title
                    }
                }
                // 하단 버튼
                GradientButton(
                    text = stringResource(id = R.string.str_add),
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(65.dp),
                    selectedColorIndex = groupColorIndex,
                ) {
                    viewModel.onEvent(
                        CreateTodoScreenEvents.CreateTodo(
                            todoId = todoMaxId,
                            groupId = groupId,
                            title = strTodoTitle.value
                        )
                    )
                }
            }
        }
    }
}

/**
 * 최상단 타이틀 - 할 일 추가
 *
 */
@Composable
private fun CreateTodoTitle(
    context: Context,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.create_todo_title))

        IconButton(onClick = {
            context.findActivity().finish()
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
            )
        }
    }
}

/**
 * 할 일 이름 설정
 */
@Composable
private fun InputTodoTitle(
    modifier: Modifier,
    strTitle: MutableState<String>,
    titleChangeEvent: (title: String) -> Unit = { _ -> },
) {
    TextField(
        modifier = modifier,
        value = strTitle.value,
        onValueChange = { titleChangeEvent(it) }
    )
}

