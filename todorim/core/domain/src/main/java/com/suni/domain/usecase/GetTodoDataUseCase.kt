package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import com.suni.data.repository.TodoDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * [UseCase] 할일 데이터 조회
 * 24.09.019 Create class - Q
 */
class GetTodoDataUseCase @Inject constructor(
    private val todoDataRepository: TodoDataRepository,
) {

    suspend fun getTodoAll(): Flow<MutableList<TodoEntity>> =
        todoDataRepository.getTodoData()

    suspend fun getTodoByGroupId(groupId: Int): Flow<MutableList<TodoEntity>> =
        todoDataRepository.getTodoDataByGroupId(groupId)

    suspend fun getTodoByTodoId(todoId: Int): Flow<TodoEntity> =
        todoDataRepository.getTodoData(todoId)

}