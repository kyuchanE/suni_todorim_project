package com.suni.todo.ui.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.TodoEntity
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

    private fun createTodo(event: CreateTodoScreenEvents.CreateTodo) {
        viewModelScope.launch {
            writeTodoDataUseCase(event.todoEntity)

            state = state.copy(
                isFinished = true
            )
        }
    }
}