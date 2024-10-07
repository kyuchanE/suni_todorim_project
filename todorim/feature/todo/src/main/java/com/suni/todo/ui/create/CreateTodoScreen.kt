package com.suni.todo.ui.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.data.model.TodoEntity
import com.suni.todo.R
import com.suni.todo.ui.component.InputTodoTitle
import com.suni.todo.ui.component.TodoTitle
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
                // TODO chan 스크롤이 있어야 하지 않을까? LazyColumn
                // 최상단 타이틀
                TodoTitle(context = context, modifier = Modifier.fillMaxWidth())
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
                            todoEntity = TodoEntity().apply {
                                this.todoId = todoMaxId
                                this.groupId = groupId
                                this.title = strTodoTitle.value
                            },
                        )
                    )
                }
            }
        }
    }
}


