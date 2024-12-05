package com.suni.todogroup.ui.todo.modify

import com.suni.data.model.TodoEntity

data class ModifyTodoScreenState(
    val isFinished: Boolean = false,
    val todoData: TodoEntity = TodoEntity(),
)