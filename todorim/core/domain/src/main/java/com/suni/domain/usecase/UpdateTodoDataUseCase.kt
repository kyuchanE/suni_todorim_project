package com.suni.domain.usecase

import android.app.AlarmManager
import android.content.Context
import com.suni.common.cancelTodoAlarm
import com.suni.data.model.TodoEntity
import com.suni.domain.setAlarmDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 수정
 * 24.09.26 Create class - Q
 */
class UpdateTodoDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val realm: Realm,
    private val scheduleAlarmManager: AlarmManager,
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

                    val isSwitchCompleted = todoData.isCompleted != findLatest(todoEntity)?.isCompleted

                    if (isSwitchCompleted) {
                        if (todoData.isCompleted) {
                            if (todoData.isDateNoti) {
                                // 할 일 완료 시 날짜 시간 알림 삭제
                                scheduleAlarmManager.cancelTodoAlarm(context, todoData.todoId)
                            }
                        } else {
                            if (todoData.isDateNoti) {
                                // 할 일 미완료 시 날짜 시간 알림 추가
                                todoData.setAlarmDateTime(context, scheduleAlarmManager)
                            }
                        }
                    }

                }
            }
    }
}