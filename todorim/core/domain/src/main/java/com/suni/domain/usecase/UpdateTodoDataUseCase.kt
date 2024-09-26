package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 수정
 * 24.09.26 Create class - Q
 */
class UpdateTodoDataUseCase @Inject constructor(
    private val realm: Realm
) {
    operator fun invoke(
        todoData: TodoEntity
    ) {
        realm.query<TodoEntity>("todoId == $0", todoData.todoId)
            .first()
            .find()
            ?.also { todoEntity ->
                realm.writeBlocking {
                    findLatest(todoEntity)?.groupId = todoData.groupId
                    findLatest(todoEntity)?.title = todoData.title
                    findLatest(todoEntity)?.isCompleted = todoData.isCompleted
                }
            }
    }
}