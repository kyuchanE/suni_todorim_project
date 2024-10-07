package com.suni.todorim.ui

import com.suni.data.model.TodoEntity

sealed class HomeScreenEvents {
    data object LoadLocalData : HomeScreenEvents()
    data class AddGroupItem(
        val groupId: Int = 0,
        val order: Int = 0,
        val title: String = "",
        val startColorHex: String = "",
        val endColorHex: String = "",
        val appColorIndex: Int = 0,
    ): HomeScreenEvents()
    data class ChangeBackground(val bgIndex: Int = 0): HomeScreenEvents()
    data class UpdateTodoData(val todoEntity: TodoEntity): HomeScreenEvents()
}