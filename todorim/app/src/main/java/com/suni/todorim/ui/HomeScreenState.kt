package com.suni.todorim.ui

import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity

data class HomeScreenState(
    val isLoading: Boolean = false,
    val groupLists: MutableList<GroupEntity> = mutableListOf(),
    val todoLists: MutableList<TodoEntity> = mutableListOf(),
    val maxGroupId: Int = 0,
    val maxTodoId: Int = 0,
    val maxOrder: Int = 0,
    val backgroundIndex: Int = 0,
)