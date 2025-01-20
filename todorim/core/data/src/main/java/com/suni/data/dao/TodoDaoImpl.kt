package com.suni.data.dao

import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.query.Sort
import javax.inject.Inject

/**
 * [TodoDao]
 * 24.09.26 Create class - Q
 */
class TodoDaoImpl @Inject constructor(
    private val realm: Realm,
) : TodoDao {

    override fun getTodoAll() =
        realm.query<TodoEntity>().find().asFlow()

    override fun getTodoByGroupId(groupId: Int) =
        realm.query<TodoEntity>("groupId = $0", groupId)
            .sort("todoId", Sort.ASCENDING)
            .find().asFlow()

    override fun getTodoByTodoId(todoId: Int) =
        realm.query<TodoEntity>("todoId = $0", todoId)
            .find().asFlow()

    override fun writeTodo(todoEntity: TodoEntity) {
        realm.writeBlocking {
            copyToRealm(todoEntity)
        }
    }

    override fun deleteTodo(query: RealmQuery<TodoEntity>) {
        realm.writeBlocking {
            delete(query)
        }
    }
}