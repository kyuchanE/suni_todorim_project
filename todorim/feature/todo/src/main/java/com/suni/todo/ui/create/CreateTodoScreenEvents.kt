package com.suni.todo.ui.create

sealed class CreateTodoScreenEvents {
    data class CreateTodo(
        val todoId: Int = 0,
        val groupId: Int = 0,
        val title: String = "",
        val isCompleted: Boolean = false,
        val order: Int = 0,
        val isDateNoti: Boolean = false,
        val date: String = "",
        val week: Int = 0,
        val day: Int = 0,
        val isLocationNoti: Boolean = false,
        val locationName: String = "",
        val longitude: Double = 0.0,
        val latitude: Double = 0.0,
        val radius: Double = 100.0,
    ) : CreateTodoScreenEvents()
}