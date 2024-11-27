package com.suni.domain.usecase

import android.app.AlarmManager
import android.content.Context
import com.suni.data.model.TodoEntity
import com.suni.domain.setAlarmDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 저장
 * 24.09.24 Create class - Q
 */
class WriteTodoDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val realm: Realm,
    private val scheduleAlarmManager: AlarmManager,
) {
    operator fun invoke(
        todoData: TodoEntity
    ) {
        realm.writeBlocking {
            copyToRealm(todoData)

            if (todoData.isDateNoti) {
                // 날짜 시간 알림
                todoData.setAlarmDateTime(context, scheduleAlarmManager)
            }

        }
    }

}