package com.suni.todo.ui.create

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.TodoEntity
import com.suni.domain.convertToString
import com.suni.domain.todoAlarmTargetCalendar
import com.suni.domain.usecase.WriteTodoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] 할 일 생성 뷰모델
 * 24.09.24 Create class - Q
 */
@HiltViewModel
class CreateTodoScreenViewModel @Inject constructor(
    private val writeTodoDataUseCase: WriteTodoDataUseCase,
): ViewModel() {

    var state by mutableStateOf(CreateTodoScreenState())
        private  set

    fun onEvent(event: CreateTodoScreenEvents) {
        when(event) {
            is CreateTodoScreenEvents.CreateTodo -> {
                createTodo(event)
            }
        }
    }

    /**
     * 할 일 생성
     */
    private fun createTodo(event: CreateTodoScreenEvents.CreateTodo) {
        viewModelScope.launch {
            with(event.todoEntity) {
                val saveTodoValue: TodoEntity = if (this.isDateNoti) {
                    setAlarmDateTime(this, todoAlarmTargetCalendar(this))
                } else {
                    this
                }
                writeTodoDataUseCase(saveTodoValue)

                state = state.copy(
                    isFinished = true
                )

            }
        }
    }

    private fun setAlarmDateTime(todoEntity: TodoEntity, targetCalendar: Calendar): TodoEntity =
        TodoEntity().apply {
            this.todoId = todoEntity.todoId
            this.groupId = todoEntity.groupId
            this.title = todoEntity.title
            this.isDateNoti = todoEntity.isDateNoti
            this.date = targetCalendar.convertToString("yyyy-MM-dd")
            this.notiTime = targetCalendar.convertToString("HH:mm")
            this.everyDay = todoEntity.everyDay
            this.day = todoEntity.day
            this.week = todoEntity.week
        }

}