package com.suni.data.utils

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.suni.common.receiver.AlarmReceiver
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_ALARM_TYPE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_NOTI_TITLE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TIME_ALARM_TYPE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TIME_VALUE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TODO_ID_FOR_REQ_CODE
import com.suni.common.receiver.AlarmReceiver.Companion.TYPE_TIME
import com.suni.common.receiver.TimeAlarmType
import com.suni.common.setNextTodoAlarm
import com.suni.data.model.TodoEntity
import kotlin.math.absoluteValue


/**
 * 할일 날짜 반복 타입
 * @param todoEntity 할일 엔티티
 */
fun alarmDateType(todoEntity: TodoEntity): TimeAlarmType =
    if (todoEntity.everyDay) {
        // 매일
        TimeAlarmType.DAY
    } else if (todoEntity.day > 0) {
        // 매월
        TimeAlarmType.MONTH
    } else if (todoEntity.week > 0) {
        // 매주
        TimeAlarmType.WEEK
    } else {
        // 반복 안함 (1회 알림 날짜)
        TimeAlarmType.ONCE
    }

/**
 * 날짜 시간 알림
 * @param context Context
 * @param alarmManager AlarmManager
 */
fun TodoEntity.setAlarmDateTime(context: Context, alarmManager: AlarmManager) {
    // 날짜 시간 알림
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra(KEY_NOTI_TITLE, title)
        putExtra(KEY_TIME_VALUE, "$date $notiTime")
        putExtra(KEY_ALARM_TYPE, TYPE_TIME)
        putExtra(KEY_TIME_ALARM_TYPE, alarmDateType(this@setAlarmDateTime).strName)
        putExtra(KEY_TODO_ID_FOR_REQ_CODE, this@setAlarmDateTime.todoId)
    }

    // 알림 시간 설정
    alarmManager.setNextTodoAlarm(
        context,
        this.todoId,
        todoAlarmTargetCalendar(this).timeInMillis,
        intent,
    )
}


/**
 * 알림 시간 설정
 * @param todoEntity 할일 엔티티
 */
fun todoAlarmTargetCalendar(todoEntity: TodoEntity): android.icu.util.Calendar {
    val returnCalendar = android.icu.util.Calendar.getInstance()
    when(alarmDateType(todoEntity)) {
        TimeAlarmType.ONCE -> {
            // 반복 안함 (1회 알림 날짜)
            val date = todoEntity.date.split("-")
            val time = todoEntity.notiTime.split(":")
            returnCalendar.set(
                date[0].toInt(),
                date[1].toInt() - 1,
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
            if (time[0].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.HOUR_OF_DAY)) {
                // 시간 비교
                returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, 1)
            } else if (time[0].toInt() == android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.HOUR_OF_DAY)) {
                // 시간 값이 동일 >> 분 비교
                if (time[1].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.MINUTE)) {
                    returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, 1)
                }
            }
            returnCalendar.set(
                returnCalendar.get(android.icu.util.Calendar.YEAR),
                returnCalendar.get(android.icu.util.Calendar.MONTH),
                returnCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH),
                time[0].toInt(),
                time[1].toInt(),
                0,
            )
        }
        TimeAlarmType.WEEK -> {
            // 매주
            val time = todoEntity.notiTime.split(":")
            // 요일 비교
            val dayOfWeek = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.DAY_OF_WEEK)
            val week = todoEntity.week
            val diff = week - dayOfWeek
            if (diff < 0) {
                returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, 7 + diff.absoluteValue)
            } else if (diff == 0) {
                if (time[0].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.HOUR_OF_DAY)) {
                    // 시간 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                    returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, 7)
                } else if (time[1].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.MINUTE)) {
                    // 분 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                    returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, 7)
                }
            } else {
                returnCalendar.add(android.icu.util.Calendar.DAY_OF_MONTH, diff)
            }
            returnCalendar.set(
                returnCalendar.get(android.icu.util.Calendar.YEAR),
                returnCalendar.get(android.icu.util.Calendar.MONTH),
                returnCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH),
                time[0].toInt(),
                time[1].toInt(),
                0,
            )
        }
        TimeAlarmType.MONTH -> {
            // 매월
            val time = todoEntity.notiTime.split(":")
            // 일 비교
            val dayOfMonth = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.DAY_OF_MONTH)
            val day = todoEntity.day
            val diff = day - dayOfMonth
            if (diff < 0) {
                returnCalendar.add(android.icu.util.Calendar.MONTH, 1)
                returnCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, day)
            } else if (diff == 0) {
                if (time[0].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.HOUR_OF_DAY)) {
                    // 시간 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                    returnCalendar.add(android.icu.util.Calendar.MONTH, 1)
                    returnCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, day)
                } else if (time[1].toInt() < android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.MINUTE)) {
                    // 분 비교 (현재 시간보다 이전 시간이면 다음날로 설정)
                    returnCalendar.add(android.icu.util.Calendar.MONTH, 1)
                    returnCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, day)
                }
            } else {
                returnCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, day)
            }
            returnCalendar.set(
                returnCalendar.get(android.icu.util.Calendar.YEAR),
                returnCalendar.get(android.icu.util.Calendar.MONTH),
                returnCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH),
                time[0].toInt(),
                time[1].toInt(),
                0,
            )
        }
    }

    return returnCalendar
}