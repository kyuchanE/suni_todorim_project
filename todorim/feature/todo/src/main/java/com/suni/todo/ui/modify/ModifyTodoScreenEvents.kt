package com.suni.todo.ui.modify

sealed class ModifyTodoScreenEvents {
    data class LoadTodoData(val todoId: Int): ModifyTodoScreenEvents()
    data class ModifyTodo(
        val todoId: Int,
        val groupId: Int,
        val title: String,
        val isCompleted: Boolean,
        val order: Int,
    ): ModifyTodoScreenEvents()
}