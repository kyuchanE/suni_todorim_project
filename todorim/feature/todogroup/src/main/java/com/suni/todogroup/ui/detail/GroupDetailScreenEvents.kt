package com.suni.todogroup.ui.detail

import com.suni.data.model.TodoEntity

sealed class GroupDetailScreenEvents {
    data class LoadGroupData(val groupId: Int, val isNeedRefresh: Boolean = false) : GroupDetailScreenEvents()
    data class UpdateTodoData(
        val todoEntity: TodoEntity,
        val finishedEvent: () -> Unit,
    ) : GroupDetailScreenEvents()

    data class DeleteTodoData(
        val todoId: Int,
        val finishedEvent: () -> Unit
    ) : GroupDetailScreenEvents()
}