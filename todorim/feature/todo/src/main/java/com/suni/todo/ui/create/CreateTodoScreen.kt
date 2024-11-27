package com.suni.todo.ui.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.data.model.TodoEntity
import com.suni.domain.toCommonTypeString
import com.suni.todo.R
import com.suni.todo.ui.component.InputTodoTitle
import com.suni.todo.ui.component.TimeAlarmContainer
import com.suni.todo.ui.component.TodoTitle
import com.suni.todo.ui.component.TypeTimeRepeating
import com.suni.ui.component.GradientButton
import java.util.Calendar

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

    var strTodoTitle by remember { mutableStateOf("") }
    var isCheckedTimeAlarm by remember { mutableStateOf(false) }
    var timeAlarmType by remember { mutableStateOf(TypeTimeRepeating.NONE) }
    var alarmTypeOptionValue by remember { mutableStateOf("") }
    var alarmTimeOptionValue by remember { mutableStateOf("") }

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
                TodoTitle(context = context, modifier = Modifier.fillMaxWidth())
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
                            strTodoTitle = title
                        }
                    }
                    item {
                        // 특정 시간 알림
                        TimeAlarmContainer(
                            modifier = Modifier.fillMaxWidth(),
                            colorIndex = groupColorIndex,
                            type = timeAlarmType,
                            isChecked = isCheckedTimeAlarm,
                            selectedTypeOption = alarmTypeOptionValue,
                            selectedTimeOption = alarmTimeOptionValue,
                            onCheckedChangedEvent = { checked ->
                                isCheckedTimeAlarm = checked
                            },
                            onTypeClickEvent = { type ->
                                timeAlarmType = type
                                alarmTypeOptionValue = ""
                                alarmTimeOptionValue = ""
                            },
                            selectedTypeOptionEvent = { typeOption ->
                                alarmTypeOptionValue = typeOption
                            },
                            selectedTimeOptionEvent = { timeOption ->
                                alarmTimeOptionValue = timeOption
                            }
                        )
                    }
                }
                // 하단 버튼 - 할 일 생성
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
                                this.title = strTodoTitle
                                this.isDateNoti = isCheckedTimeAlarm
                                this.notiTime = alarmTimeOptionValue
                                when(timeAlarmType) {
                                    TypeTimeRepeating.NONE -> {
                                        // 반복 안함 (특정 날짜)
                                        this.date = if(alarmTypeOptionValue.isEmpty()) {
                                            Calendar.getInstance().time.toCommonTypeString() ?: ""
                                        } else {
                                            alarmTypeOptionValue
                                        }
                                    }
                                    TypeTimeRepeating.MONTH -> {
                                        // 매월
                                        this.day = alarmTypeOptionValue.toInt()
                                    }
                                    TypeTimeRepeating.WEEK -> {
                                        // 매주
                                        this.week = alarmTypeOptionValue.toInt()
                                    }
                                    TypeTimeRepeating.DAY -> {
                                        // 매일
                                        this.everyDay = true
                                    }
                                }
                            },
                        )
                    )
                }
            }
        }
    }
}


