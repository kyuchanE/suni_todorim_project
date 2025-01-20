package com.suni.data.repository

import android.app.AlarmManager
import android.content.Context
import com.suni.common.cancelTodoAlarm
import com.suni.data.dao.TodoDao
import com.suni.data.model.TodoEntity
import com.suni.data.utils.setAlarmDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [TodoDataRepository]
 * 24.09.26 Create class - Q
 */
class TodoDataRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val realm: Realm,
    private val scheduleAlarmManager: AlarmManager,
    private val context: Context,
) : TodoDataRepository {

    /**
     * 할일 데이터 전체 조회
     * @param todoId 할일 아이디
     */
    override suspend fun getTodoData(): Flow<MutableList<TodoEntity>> =
        todoDao.getTodoAll().map { resultsChange ->
            resultsChange.list.toMutableList()
        }

    /**
     * 할일 데이터 조회
     * @param todoId 할일 아이디
     */
    override suspend fun getTodoData(todoId: Int): Flow<TodoEntity> =
        todoDao.getTodoByTodoId(todoId).map { resultsChange ->
            resultsChange.list.first()
        }

    /**
     * 그룹 아이디로 할일 데이터 조회
     * @param groupId 그룹 아이디
     */
    override suspend fun getTodoDataByGroupId(
        groupId: Int
    ): Flow<MutableList<TodoEntity>> =
        todoDao.getTodoByGroupId(groupId).map { resultsChange ->
            resultsChange.list.toMutableList()
        }

    /**
     * 할일 데이터 추가
     * @param todoEntity 할일 엔티티
     */
    override fun writeTodoData(todoEntity: TodoEntity) {
        todoDao.writeTodo(todoEntity)
    }

    /**
     * 할일 데이터 업데이트
     * @param updateEntity 업데이트 할 할일 엔티티
     */
    override suspend fun updateTodoData(updateEntity: TodoEntity) {
        todoDao.getTodoByGroupId(updateEntity.todoId).collectLatest { resultsChange ->
            resultsChange.list.last().let { todoEntity ->
                realm.writeBlocking {
                    findLatest(todoEntity)?.groupId = updateEntity.groupId
                    findLatest(todoEntity)?.title = updateEntity.title
                    findLatest(todoEntity)?.isCompleted = updateEntity.isCompleted

                    val isSwitchCompleted = updateEntity.isCompleted != findLatest(todoEntity)?.isCompleted

                    if (isSwitchCompleted) {
                        if (updateEntity.isCompleted) {
                            if (updateEntity.isDateNoti) {
                                // 할 일 완료 시 날짜 시간 알림 삭제
                                scheduleAlarmManager.cancelTodoAlarm(context, updateEntity.todoId)
                            }
                        } else {
                            if (updateEntity.isDateNoti) {
                                // 할 일 미완료 시 날짜 시간 알림 추가
                                updateEntity.setAlarmDateTime(context, scheduleAlarmManager)
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 할일 데이터 삭제
     * @param todoId 할일 아이디
     */
    override suspend fun deleteTodoData(todoId: Int) {
        todoDao.getTodoByTodoId(todoId).map { resultsChange ->
            realm.writeBlocking {
                val query = this.query<TodoEntity>("todoId == $0", todoId)

                resultsChange.list.first().let {
                    if (it.isDateNoti) {
                        // 할 일 완료 시 날짜 시간 알림 삭제
                        scheduleAlarmManager.cancelTodoAlarm(context, todoId)
                    }
                }

                todoDao.deleteTodo(query)
            }
        }
    }
}