package com.suni.domain

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
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
import com.suni.ui.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.absoluteValue

/**
 * 그라데이션 start color
 */
fun Int.getGradientStartColor(): Int {
    return when(this) {
        1 -> R.color.gradient_start_1
        2 -> R.color.gradient_start_2
        3 -> R.color.gradient_start_3
        4 -> R.color.gradient_start_4
        5 -> R.color.gradient_start_5
        6 -> R.color.gradient_start_6
        7 -> R.color.gradient_start_7
        8 -> R.color.gradient_start_8
        9 -> R.color.gradient_start_9
        else -> R.color.tdr_default
    }
}

/**
 * 그라데이션 end color
 */
fun Int.getGradientEndColor(): Int {
    return when(this) {
        1 -> R.color.gradient_end_1
        2 -> R.color.gradient_end_2
        3 -> R.color.gradient_end_3
        4 -> R.color.gradient_end_4
        5 -> R.color.gradient_end_5
        6 -> R.color.gradient_end_6
        7 -> R.color.gradient_end_7
        8 -> R.color.gradient_end_8
        9 -> R.color.gradient_end_9
        else -> R.color.tdr_default
    }
}

fun String.getTimeNow(): String {
    return try {
        val date = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat(this)
        simpleDateFormat.format(date)
    } catch (e: Exception) {
        L.e(e.message)
        ""
    }
}

/**
 * SimpleDateFormat Default : "yyyyMMdd"
 * @return Date
 */
fun String.toDate(pattern: String = "yyyyMMdd"): Date? {
    return try {
        SimpleDateFormat(pattern).parse(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * 요일 변환
 */
fun String.convertToStrWeek(): String {
    return when(this) {
        "1" -> "일요일"
        "2" -> "월요일"
        "3" -> "화요일"
        "4" -> "수요일"
        "5" -> "목요일"
        "6" -> "금요일"
        "7" -> "토요일"
        else -> this
    }
}

/**
 * 월 변환
 */
fun String.convertToStrMonth(): String = this + "일"

/**
 * @return String 메인 월/일 표시
 */
fun getStrHomeDate(): String {
    return try {
        val now = "MM/dd".getTimeNow().split("/")
        return "${now[0]}월 ${now[1].toInt()}일"
    } catch (e: Exception) {
        L.e(e.message)
        ""
    }
}

/**
 * @return String 요일 표시
 */
fun getDayOfWeek(): String {
    return try {
        // TODO chan 이상한디...
        val cal = Calendar.getInstance()
        cal.time = Date()
        L.d("getDayOfWeek today : ${cal.get(Calendar.DAY_OF_WEEK)}")

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val targetDay = "yyyyMMdd".getTimeNow().toLong()
        val today = simpleDateFormat.format(cal.time).toLong()

        val moveDate = targetDay - today
        cal.add(Calendar.DATE, moveDate.toInt())

        L.d("getDayOfWeek targetDay : ${cal.get(Calendar.DAY_OF_WEEK)}")
        when(cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "일요일"
            2 -> "월요일"
            3 -> "화요일"
            4 -> "수요일"
            5 -> "목요일"
            6 -> "금요일"
            7 -> "토요일"
            else -> ""
        }
    } catch (e: Exception) {
        L.e(e.message)
        ""
    }
}

/**
 * @return String 요일 표시
 */
fun String.getNow(): Calendar {
    val cal = Calendar.getInstance()
    try {
        cal.time = SimpleDateFormat(this).parse(this)
    } catch (e: Exception) {
        L.e(e.message)
    }
    return cal
}

/**
 * @return String
 */
fun android.icu.util.Calendar.convertToString(pattern: String): String {
    return try {
        SimpleDateFormat(pattern).format(this.time)
    } catch (e: Exception) {
        L.e(e.message)
        ""
    }
}

/**
 *
 */
fun Date.toFullString(): String? {
    return try {
        val calendar = Calendar.getInstance()
        calendar.time = this

        "${calendar.get(Calendar.YEAR)}년 " +
                "${calendar.get(Calendar.MONTH) + 1}월 " +
                "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                "(${calendar.get(Calendar.DAY_OF_WEEK).getDayOfWeek()})"
    } catch (e: Exception) {
        null
    }
}

/**
 * return to String (yyyy-MM-dd)
 */
fun Date.toCommonTypeString(): String? {
    return try {
        val calendar = Calendar.getInstance()
        calendar.time = this

        "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DAY_OF_MONTH)}"
    } catch (e: Exception) {
        null
    }
}

fun Int.getDayOfWeek(): String {
    return when(this) {
        1 -> "일요일"
        2 -> "월요일"
        3 -> "화요일"
        4 -> "수요일"
        5 -> "목요일"
        6 -> "금요일"
        7 -> "토요일"
        else -> ""
    }
}

/**
 * @return Activity
 */
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

inline fun <reified T: Activity> Activity.startActivityWithFinish(
    intentBuilder: Intent.() -> Intent = { this },
    withFinish: Boolean = true
) {
    startActivity(Intent(this, T::class.java).intentBuilder())
    if (withFinish) finish()
}

inline fun <reified T: Activity> Activity.resultLauncherWithFinish(
    intentBuilder: Intent.() -> Intent = { this },
    activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    withFinish: Boolean = true
) {
    activityResultLauncher.launch(
        Intent(this, T::class.java).intentBuilder()
    )
    if (withFinish) finish()
}

/**
 * 날짜 시간 알림
 * @param context Context
 * @param alarmManager AlarmManager
 */
fun TodoEntity.setAlarmDateTime(context: Context, alarmManager: AlarmManager) {
    // 날짜 시간 알림
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        L.d("setAlarmDateTime KEY_TIME_VALUE : $date $notiTime")
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
    L.d("alarmCalendar ${returnCalendar.get(android.icu.util.Calendar.YEAR)} : ${returnCalendar.get(
        android.icu.util.Calendar.MONTH) + 1} : ${returnCalendar.get(
        android.icu.util.Calendar.DAY_OF_MONTH)} , ${returnCalendar.get(android.icu.util.Calendar.HOUR_OF_DAY)} : ${returnCalendar.get(
        android.icu.util.Calendar.MINUTE)}")
    return returnCalendar
}

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