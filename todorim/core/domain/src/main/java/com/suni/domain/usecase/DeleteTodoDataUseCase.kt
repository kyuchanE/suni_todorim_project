package com.suni.domain.usecase

import com.suni.data.repository.TodoDataRepository
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 삭제
 * 24.09.26 Create class - Q
 */
class DeleteTodoDataUseCase @Inject constructor(
    private val todoDataRepository: TodoDataRepository,
) {
   suspend operator fun invoke(todoId: Int) {
       todoDataRepository.deleteTodoData(todoId)
   }
}