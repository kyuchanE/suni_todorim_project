package com.suni.common.receiver

import android.Manifest
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.suni.common.R
import com.suni.common.cancelTodoAlarm
import com.suni.common.convertToCalendar
import com.suni.common.convertToString
import com.suni.common.setNextTodoAlarm
import java.util.Calendar

enum class TimeAlarmType(val strName: String) {
    ONCE("TIME_ALARM_ONCE"),       // 반복 없음
    DAY("TIME_ALARM_DAY"),        // 매일
    WEEK("TIME_ALARM_WEEK"),       // 매주
    MONTH("TIME_ALARM_MONTH"),      // 매월
}

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_TIME = "TYPE_TIME"

        const val KEY_ALARM_TYPE = "KEY_ALARM_TYPE"     // 시간 알림 / 위치 알림
        const val KEY_TIME_ALARM_TYPE = "KEY_TIME_ALARM_TYPE"       // 매일 / 매주 / 매월
        const val KEY_NOTI_TITLE = "KEY_NOTI_TITLE"                 // 알림 제목
        const val KEY_TIME_VALUE = "KEY_TIME_VALUE"         // yyyy-mm-dd hh:mm
        const val KEY_TODO_ID_FOR_REQ_CODE = "KEY_TODO_ID_FOR_REQ_CODE"     // 할일 ID
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO chan 메인 이동 또는 해당 알림 설정으로 이동 해야함 - 어떻게 해당 엑티비티 값을 가져올까?
//        val routeIntent = Intent(context, MainActivity::class.java)

        context?.let {
            val scheduleAlarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val title = intent?.getStringExtra(KEY_NOTI_TITLE) ?: "투두림 알림"
                val alarmType = intent?.getStringExtra(KEY_ALARM_TYPE) ?: ""        // 시간 알림 / 위치 알림
                val timeAlarmType = intent?.getStringExtra(KEY_TIME_ALARM_TYPE) ?: ""
                val alarmTime = intent?.getStringExtra(KEY_TIME_VALUE) ?: ""
                val todoId = intent?.getIntExtra(KEY_TODO_ID_FOR_REQ_CODE, 0) ?: 0

                val builder = NotificationCompat.Builder(it, it.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_todorim_launcher)
                    .setContentTitle(title)
                    .setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                NotificationManagerCompat.from(it).notify(todoId, builder.build())

                scheduleAlarmManager.let { alarmManager ->
                    // 알람 해제
                    alarmManager.cancelTodoAlarm(it, todoId)

                    when (alarmType) {
                        TYPE_TIME -> {
                            if (alarmTime.isNotEmpty()) {
                                // 다음 알람 설정
                                setNextTimeAlarm(
                                    context = it,
                                    alarmManager = alarmManager,
                                    alarmTime = alarmTime,
                                    timeAlarmType = timeAlarmType,
                                    alarmType = alarmType,
                                    todoId = todoId,
                                    title = title,
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 다음 알람 설정
     * @param context
     * @param alarmManager 알람 매니저
     * @param alarmTime 알람 시간
     * @param timeAlarmType 알람 시간 타입
     * @param alarmType 알람 타입
     * @param todoId 할일 ID
     * @param title 할일 제목
     */
    private fun setNextTimeAlarm(
        context: Context,
        alarmManager: AlarmManager,
        alarmTime: String,
        timeAlarmType: String,
        alarmType: String,
        todoId: Int,
        title: String,
    ) {
        val targetCalendar = alarmTime.convertToCalendar("yyyy-MM-dd hh:mm")
        run timeAlarmLoop@{
            when (timeAlarmType) {
                TimeAlarmType.ONCE.strName -> {
                    // 한번만 알림
                    return@timeAlarmLoop
                }
                TimeAlarmType.DAY.strName -> {
                    // 매일 알림
                    // 다음날 알람 설정
                    targetCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                TimeAlarmType.WEEK.strName -> {
                    // 매주 알림
                    // 다음주 알람 설정
                    targetCalendar.add(Calendar.DAY_OF_MONTH, 7)
                }
                TimeAlarmType.MONTH.strName -> {
                    // 매월 알림
                    // 다음달 알람 설정
                    targetCalendar.add(Calendar.MONTH, 1)
                }
            }
            alarmManager.setNextTodoAlarm(
                context = context,
                targetCalendar = targetCalendar,
                todoId = todoId,
                todoTitle = title,
                alarmType = alarmType,
                timeValue = targetCalendar.convertToString("yyyy-MM-dd hh:mm"),
                timeAlarmType = timeAlarmType,
            )
        }
    }

}
