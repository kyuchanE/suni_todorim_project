package com.suni.todogroup.ui.todo.create

import com.suni.data.model.TodoEntity

sealed class CreateTodoScreenEvents {
    data class CreateTodo(
        val todoEntity: TodoEntity,
    ) : CreateTodoScreenEvents()
}