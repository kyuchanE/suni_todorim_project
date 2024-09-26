package com.suni.todo.ui.create

import com.suni.data.model.TodoEntity

sealed class CreateTodoScreenEvents {
    data class CreateTodo(
        val todoEntity: TodoEntity,
    ) : CreateTodoScreenEvents()
}