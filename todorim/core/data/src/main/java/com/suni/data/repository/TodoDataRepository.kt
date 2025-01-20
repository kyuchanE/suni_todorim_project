package com.suni.data.repository

import com.suni.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoDataRepository {

    suspend fun getTodoData(): Flow<MutableList<TodoEntity>>

    suspend fun getTodoData(todoId: Int): Flow<TodoEntity>

    suspend fun getTodoDataByGroupId(groupId: Int): Flow<MutableList<TodoEntity>>

    fun writeTodoData(todoEntity: TodoEntity)

    suspend fun updateTodoData(updateEntity: TodoEntity)

    suspend fun deleteTodoData(todoId: Int)
}