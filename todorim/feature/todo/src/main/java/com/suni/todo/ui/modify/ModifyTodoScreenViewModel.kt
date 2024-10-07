package com.suni.todo.ui.modify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.TodoEntity
import com.suni.domain.usecase.GetTodoDataUseCase
import com.suni.domain.usecase.UpdateTodoDataUseCase
import com.suni.domain.usecase.WriteTodoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] 할 일 수정 뷰모델
 * 24.10.07 Create class - Q
 */
@HiltViewModel
class ModifyTodoScreenViewModel @Inject constructor(
    val updateTodoDataUseCase: UpdateTodoDataUseCase,
    val getTodoDataUseCase: GetTodoDataUseCase,
): ViewModel() {

    var state by mutableStateOf(ModifyTodoScreenState())
        private set

    fun onEvent(events: ModifyTodoScreenEvents) {
        when(events) {
            is ModifyTodoScreenEvents.LoadTodoData -> {
                getTodoData(events)
            }
            is ModifyTodoScreenEvents.ModifyTodo -> {
                updateTodo(events)
            }
        }
    }

    /**
     * 할 일 수정
     * @param events
     */
    private fun updateTodo(events: ModifyTodoScreenEvents.ModifyTodo) {
        viewModelScope.launch {
            updateTodoDataUseCase(
                TodoEntity().apply {
                    todoId = events.todoId
                    groupId = events.groupId
                    title = events.title
                    order = events.order
                    isCompleted = events.isCompleted
                }
            )
            state = state.copy(
                isFinished = true
            )
        }
    }

    /**
     * 할 일 데이터 조회
     * @param event
     */
    private fun getTodoData(event: ModifyTodoScreenEvents.LoadTodoData) {
        viewModelScope.launch {
            val result = getTodoDataUseCase.getTodoByGroupId(event.todoId)

            if (result.isNotEmpty()) {
               state = state.copy(
                   todoData = result.first()
               )
            }
        }
    }


}