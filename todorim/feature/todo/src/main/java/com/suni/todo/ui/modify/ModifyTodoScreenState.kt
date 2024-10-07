package com.suni.todo.ui.modify

import com.suni.data.model.TodoEntity

data class ModifyTodoScreenState(
    val isFinished: Boolean = false,
    val todoData: TodoEntity = TodoEntity(),
)