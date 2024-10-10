package com.suni.domain.usecase

import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import javax.inject.Inject

/**
 * [UseCase] 할일 데이터 조회
 * 24.09.019 Create class - Q
 */
class GetTodoDataUseCase @Inject constructor(
    private val realm: Realm,
) {

    fun getTodoAll(): RealmResults<TodoEntity> =
        realm.query<TodoEntity>().find()

    fun getTodoByGroupId(groupId: Int): RealmResults<TodoEntity> =
        realm.query<TodoEntity>("groupId = $0", groupId).find()

    fun getTodoByTodoId(todoId: Int): RealmResults<TodoEntity> =
        realm.query<TodoEntity>("todoId = $0", todoId).find()

}