package com.suni.domain.usecase

import android.app.AlarmManager
import android.content.Context
import com.suni.common.cancelTodoAlarm
import com.suni.data.model.TodoEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 삭제
 * 24.09.26 Create class - Q
 */
class DeleteTodoDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val realm: Realm,
    private val scheduleAlarmManager: AlarmManager,
) {
   operator fun invoke(todoId: Int) {
       realm.writeBlocking {
           val resultQuery = this.query<TodoEntity>("todoId == $0", todoId)
           resultQuery.find().first().let {
               if (it.isDateNoti) {
                   // 할 일 완료 시 날짜 시간 알림 삭제
                   scheduleAlarmManager.cancelTodoAlarm(context, todoId)
               }
           }
           delete(resultQuery)
       }
   }
}