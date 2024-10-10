package com.suni.todo.ui.modify

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.domain.findActivity
import com.suni.todo.R
import com.suni.todo.ui.component.InputTodoTitle
import com.suni.todo.ui.component.TodoTitle
import com.suni.ui.component.GradientButton
import com.suni.ui.component.TimeAlarmContainer
import com.suni.ui.component.TypeTimeRepeating

@Composable
fun ModifyTodoScreen(
    viewModel: ModifyTodoScreenViewModel,
    todoId: Int,
    groupColorIndex: Int,
    finishAndRefreshActivityAction: () -> Unit = {},
) {
    val context = LocalContext.current

    val strTodoTitle = remember { mutableStateOf("") }
    val timeAlarm = remember { mutableStateOf(false) }
    val timeAlarmType = remember { mutableStateOf(TypeTimeRepeating.NONE) }

    LaunchedEffect(Unit) {
        // 할 일 정보 조회
        viewModel.onEvent(ModifyTodoScreenEvents.LoadTodoData(todoId))
        strTodoTitle.value = viewModel.state.todoData.title
    }

    LaunchedEffect(viewModel.state.isFinished) {
        // 수정 완료 후
        if (viewModel.state.isFinished)
            finishAndRefreshActivityAction()
    }

    Scaffold { pv ->
        Surface(
            modifier = Modifier.padding(pv),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // 최상단 타이틀
                TodoTitle(
                    context = context,
                    modifier = Modifier.fillMaxWidth(),
                    isCreateMode = false,
                )
                // 할 일 정보 기입
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    item {
                        // 할 일 이름
                        InputTodoTitle(
                            modifier = Modifier.fillMaxWidth(),
                            strTitle = strTodoTitle,
                        ) { title ->
                            strTodoTitle.value = title
                        }
                    }
                    item {
                        // 특정 시간 알림
                        TimeAlarmContainer(
                            modifier = Modifier.fillMaxWidth(),
                            colorIndex = groupColorIndex,
                            type = timeAlarmType.value,
                            isChecked = timeAlarm.value,
                            onCheckedChangedEvent = { checked ->
                                timeAlarm.value = checked
                            },
                            onTypeClickEvent = { type ->
                                timeAlarmType.value = type
                            }
                        )
                    }
                }
                // 하단 버튼
                GradientButton(
                    text = stringResource(id = R.string.str_modify),
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(65.dp),
                    selectedColorIndex = groupColorIndex,
                ) {
                    viewModel.onEvent(
                        ModifyTodoScreenEvents.ModifyTodo(
                            todoId = todoId,
                            groupId = viewModel.state.todoData.groupId,
                            title = strTodoTitle.value,
                            order = viewModel.state.todoData.order,
                            isCompleted = viewModel.state.todoData.isCompleted,
                        )
                    )
                }
            }

        }
    }
}