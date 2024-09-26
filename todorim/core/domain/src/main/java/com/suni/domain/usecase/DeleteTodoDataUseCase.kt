package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 삭제
 * 24.09.26 Create class - Q
 */
class DeleteTodoDataUseCase @Inject constructor(
    private val realm: Realm
) {
   operator fun invoke(todoId: Int) {
       realm.writeBlocking {
           val resultQuery = this.query<TodoEntity>("todoId == $0", todoId)
           delete(resultQuery)
       }
   }
}