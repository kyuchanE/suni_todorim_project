package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 저장
 * 24.09.24 Create class - Q
 */
class WriteTodoDataUseCase @Inject constructor(
    private val realm: Realm
) {
    operator fun invoke(
        todoData: TodoEntity
    ) {
        realm.writeBlocking {
            copyToRealm(todoData)
        }
    }
}