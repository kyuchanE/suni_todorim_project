package com.suni.todo.ui.create

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.common.receiver.AlarmReceiver
import com.suni.common.receiver.TimeAlarmType
import com.suni.data.model.TodoEntity
import com.suni.domain.usecase.WriteTodoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

/**
 * [ViewModel] 할 일 생성 뷰모델
 * 24.09.24 Create class - Q
 */
@HiltViewModel
class CreateTodoScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val writeTodoDataUseCase: WriteTodoDataUseCase,
    private val scheduleAlarmManager: AlarmManager,
): ViewModel() {

    var state by mutableStateOf(CreateTodoScreenState())
        private  set

    fun onEvent(event: CreateTodoScreenEvents) {
        when(event) {
            is CreateTodoScreenEvents.CreateTodo -> {
                createTodo(event)
            }
        }
    }

    /**
     * 할 일 생성
     */
    private fun createTodo(event: CreateTodoScreenEvents.CreateTodo) {
        viewModelScope.launch {
            with(event.todoEntity) {
                writeTodoDataUseCase(this)

                if (this.isDateNoti) {
                    // 날짜 시간 알림
                    scheduleAlarmManager.let { aManager ->
                        val intent = Intent(context, AlarmReceiver::class.java).apply {
                            putExtra("TEST", this@with.title)
                        }
                        val pendingIntent =
                            PendingIntent.getService(
                                context,
                                this.todoId,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT,
                            )
                        if (pendingIntent != null) {
                            aManager.cancel(pendingIntent)
                        }

                        // 알림 시간 설정
                        aManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            targetCalendar(this).timeInMillis,
                            pendingIntent,
                        )
                    }
                }

                state = state.copy(
                    isFinished = true
                )
            }
        }
    }

    /**
     * 알림 시간 설정
     * @param todoEntity 할일 엔티티
     */
    private fun targetCalendar(todoEntity: TodoEntity): Calendar {
        val returnCalendar = Calendar.getInstance()
        when(alarmDateType(todoEntity)) {
            TimeAlarmType.ONCE -> {
                // 반복 안함 (1회 알림 날짜)
                val date = todoEntity.date.split("-")
                val time = todoEntity.notiTime.split(":")
                returnCalendar.set(
                    date[0].toInt(),
                    date[1].toInt(),
                    date[2].toInt(),
                    time[0].toInt(),
                    time[1].toInt(),
                    0,
                )
            }
            TimeAlarmType.DAY -> {
                // 매일
                val time = todoEntity.notiTime.split(":")
                // 시간 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                if (time[0].toInt() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    returnCalendar.add(Calendar.DAY_OF_MONTH, 1)
                } else if (time[1].toInt() < Calendar.getInstance().get(Calendar.MINUTE)) {
                    // 분 비교
                    returnCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                returnCalendar.set(
                    returnCalendar.get(Calendar.YEAR),
                    returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DAY_OF_MONTH),
                    time[0].toInt(),
                    time[1].toInt(),
                    0,
                )
            }
            TimeAlarmType.WEEK -> {
                // 매주
                val time = todoEntity.notiTime.split(":")
                // 요일 비교
                val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                val week = todoEntity.week
                val diff = week - dayOfWeek
                if (diff < 0) {
                    returnCalendar.add(Calendar.DAY_OF_MONTH, 7 + diff.absoluteValue)
                } else if (diff == 0) {
                    if (time[0].toInt() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        // 시간 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                        returnCalendar.add(Calendar.DAY_OF_MONTH, 7)
                    } else if (time[1].toInt() < Calendar.getInstance().get(Calendar.MINUTE)) {
                        // 분 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                        returnCalendar.add(Calendar.DAY_OF_MONTH, 7)
                    }
                } else {
                    returnCalendar.add(Calendar.DAY_OF_MONTH, diff)
                }
                returnCalendar.set(
                    returnCalendar.get(Calendar.YEAR),
                    returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DAY_OF_MONTH),
                    time[0].toInt(),
                    time[1].toInt(),
                    0,
                )
            }
            TimeAlarmType.MONTH -> {
                // 매월
                val time = todoEntity.notiTime.split(":")
                // 일 비교
                val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val day = todoEntity.day
                val diff = day - dayOfMonth
                if (diff < 0) {
                    returnCalendar.add(Calendar.MONTH, 1)
                    returnCalendar.set(Calendar.DAY_OF_MONTH, day)
                } else if (diff == 0) {
                    if (time[0].toInt() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        // 시간 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                        returnCalendar.add(Calendar.MONTH, 1)
                        returnCalendar.set(Calendar.DAY_OF_MONTH, day)
                    } else if (time[1].toInt() < Calendar.getInstance().get(Calendar.MINUTE)) {
                        // 분 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                        returnCalendar.add(Calendar.MONTH, 1)
                        returnCalendar.set(Calendar.DAY_OF_MONTH, day)
                    }
                } else {
                    returnCalendar.set(Calendar.DAY_OF_MONTH, day)
                }
                returnCalendar.set(
                    returnCalendar.get(Calendar.YEAR),
                    returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DAY_OF_MONTH),
                    time[0].toInt(),
                    time[1].toInt(),
                    0,
                )
            }
        }

        return returnCalendar
    }

    /**
     * 할일 날짜 반복 타입
     * @param todoEntity 할일 엔티티
     */
    private fun alarmDateType(todoEntity: TodoEntity): TimeAlarmType =
        if (todoEntity.date.isNotEmpty()) {
            // 반복 안함 (1회 알림 날짜)
            TimeAlarmType.ONCE
        } else if (todoEntity.day > 0) {
            // 매월
            TimeAlarmType.MONTH
        } else if (todoEntity.week > 0) {
            // 매주
            TimeAlarmType.WEEK
        } else {
            // 매일
            TimeAlarmType.DAY
        }

}