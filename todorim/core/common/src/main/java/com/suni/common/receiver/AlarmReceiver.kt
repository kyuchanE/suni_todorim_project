package com.suni.common.receiver

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.suni.common.R
import java.util.Calendar
import javax.inject.Inject

enum class TimeAlarmType(val strName: String) {
    ONCE("TIME_ALARM_ONCE"),       // 반복 없음
    DAY("TIME_ALARM_DAY"),        // 매일
    WEEK("TIME_ALARM_WEEK"),       // 매주
    MONTH("TIME_ALARM_MONTH"),      // 매월
}

class AlarmReceiver @Inject constructor(
    private val scheduleAlarmManager: AlarmManager,
) : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1113
        const val CHANNEL_ID = "TODORIM_ALARM"

        const val TYPE_TIME = "TYPE_TIME"

        const val KEY_ALARM_TYPE = "KEY_ALARM_TYPE"
        const val KEY_TIME_ALARM_TYPE = "KEY_TIME_ALARM_TYPE"
        const val KEY_NOTI_TITLE = "KEY_NOTI_TITLE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO chan 메인 이동 또는 해당 알림 설정으로 이동 해야함 - 어떻게 해당 엑티비티 값을 가져올까?
//        val routeIntent = Intent(context, MainActivity::class.java)

        context?.let {
            if (
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val title = intent?.getStringExtra(KEY_NOTI_TITLE) ?: "투두림 알림"
                val alarmType = intent?.getStringExtra(KEY_ALARM_TYPE) ?: ""
                val timeAlarmType = intent?.getStringExtra(KEY_TIME_ALARM_TYPE) ?: ""


                val builder = NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_todorim_launcher)
                    .setContentTitle(title)
                    .setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                NotificationManagerCompat.from(it).notify(NOTIFICATION_ID, builder.build())

                scheduleAlarmManager.let { alarmManager ->
                    val targetCalendar = Calendar.getInstance()
                    when (alarmType) {
                        TYPE_TIME -> {
                            when (timeAlarmType) {
                                TimeAlarmType.ONCE.strName -> {
                                    // 한번만 알림
                                    // 알람 해제
                                    alarmManager.cancel(
                                        PendingIntent.getBroadcast(
                                            it,
                                            0,
                                            Intent(it, AlarmReceiver::class.java),
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                        )
                                    )
                                }

                                TimeAlarmType.DAY.strName -> {
                                    // 매일 알림
                                    // 다음날 알람 설정
                                    targetCalendar.add(Calendar.DAY_OF_MONTH, 1)
                                    setNextAlarm(targetCalendar, it)
                                }

                                TimeAlarmType.WEEK.strName -> {
                                    // 매주 알림
                                    // 다음주 알람 설정
                                    targetCalendar.add(Calendar.DAY_OF_MONTH, 7)
                                    setNextAlarm(targetCalendar, it)
                                }

                                TimeAlarmType.MONTH.strName -> {
                                    // 매월 알림
                                    // 다음달 알람 설정
                                    targetCalendar.add(Calendar.MONTH, 1)
                                    setNextAlarm(targetCalendar, it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 다음 반복 알림 설정
     * @param targetCalendar 다음 알람 시간
     * @param context
     */
    private fun setNextAlarm(targetCalendar: Calendar, context: Context) {
        // 다음 알람 설정
        scheduleAlarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            targetCalendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

}