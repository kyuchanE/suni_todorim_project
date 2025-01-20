package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import com.suni.data.repository.TodoDataRepository
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 수정
 * 24.09.26 Create class - Q
 */
class UpdateTodoDataUseCase @Inject constructor(
    private val todoDataRepository: TodoDataRepository,
) {
    suspend operator fun invoke(
        todoData: TodoEntity
    ) {
        todoDataRepository.updateTodoData(todoData)
    }
}