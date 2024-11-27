package com.suni.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.suni.common.receiver.AlarmReceiver
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_ALARM_TYPE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_NOTI_TITLE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TIME_ALARM_TYPE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TIME_VALUE
import com.suni.common.receiver.AlarmReceiver.Companion.KEY_TODO_ID_FOR_REQ_CODE
import java.text.SimpleDateFormat
import java.util.Calendar


/**
 * 할일 알람 해제
 * @param context
 * @param todoId 할일 ID
 */
fun AlarmManager.cancelTodoAlarm(context: Context, todoId: Int) {
    Log.d("@@@@@@", "AlarmReceiver cancelTodoAlarm >> $todoId")
    this.cancel(
        PendingIntent.getBroadcast(
            context,
            todoId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_MUTABLE
        )
    )
}

/**
 * 다음 반복 알림 설정
 * @param context
 * @param targetCalendar 다음 알람 시간
 * @param alarmType  알림 타입 (시간 알림 / 위치 알림)
 * @param timeValue  알림 시간 값 (yyyy-mm-dd hh:mm)
 * @param timeAlarmType  알림 시간 타입 (매일 / 매주 / 매월)
 * @param todoId 할일 ID
 * @param todoTitle 할일 제목
 */
fun AlarmManager.setNextTodoAlarm(
    context: Context,
    targetCalendar: Calendar,
    todoId: Int,
    todoTitle: String,
    alarmType: String,
    timeValue: String,
    timeAlarmType: String,
) {
    // 다음 알람 설정
    setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        targetCalendar.timeInMillis,
        PendingIntent.getBroadcast(
            context,
            todoId,
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(KEY_NOTI_TITLE, todoTitle)
                putExtra(KEY_TIME_VALUE, timeValue)
                putExtra(KEY_ALARM_TYPE, alarmType)
                putExtra(KEY_TIME_ALARM_TYPE, timeAlarmType)
                putExtra(KEY_TODO_ID_FOR_REQ_CODE, todoId)
            },
            PendingIntent.FLAG_MUTABLE
        )
    )
}

/**
 * 다음 반복 알림 설정
 * @param context
 * @param todoId 할일 ID
 * @param targetCalendarMillis 다음 알람 시간
 * @param intent 알람 인텐트
 */
fun AlarmManager.setNextTodoAlarm(
    context: Context,
    todoId: Int,
    targetCalendarMillis: Long,
    intent: Intent,
) {
    // 다음 알람 설정
    setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        targetCalendarMillis,
        PendingIntent.getBroadcast(
            context,
            todoId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    )
}

fun String.convertToCalendar(patterns: String): Calendar {
    val cal = Calendar.getInstance()
    try {
        cal.time = SimpleDateFormat(patterns).parse(this)
    } catch (e: Exception) {
        Log.e("AlarmReceiver convert to Calendar", e.message?:"")
    }
    return cal
}

fun Calendar.convertToString(patterns: String): String {
    return try {
        SimpleDateFormat(patterns).format(this.time)
    } catch (e: Exception) {
        Log.e("AlarmReceiver convert to String", e.message?:"")
        ""
    }
}