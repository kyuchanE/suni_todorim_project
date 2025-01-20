package com.suni.data.dao

import com.suni.data.model.TodoEntity
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmQuery
import kotlinx.coroutines.flow.Flow

interface TodoDao {
    // 모든 할 일 데이터 조회
    fun getTodoAll(): Flow<ResultsChange<TodoEntity>>
    // 특정 할 일 데이터 조회 (그룹id 기준)
    fun getTodoByGroupId(groupId: Int): Flow<ResultsChange<TodoEntity>>
    // 특정 할 일 데이터 조회 (할 일 id 기준)
    fun getTodoByTodoId(todoId: Int): Flow<ResultsChange<TodoEntity>>
    // 할 일 데이터 추가
    fun writeTodo(todoEntity: TodoEntity)
    // 그룹 데이터 삭제
    fun deleteTodo(query: RealmQuery<TodoEntity>)
}