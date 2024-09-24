package com.suni.domain

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.suni.ui.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
 * @return String 메인 월/일 표시
 */
fun getStrHomeDate(): String {
    return try {
        val now = "MM/dd".getTimeNow().split("/")
        return "${now[0]}월 ${now[1]}일"
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

inline fun <reified T: Activity> Activity.startActivityWithAnimation(
    intentBuilder: Intent.() -> Intent = { this },
    withFinish: Boolean = true
) {
    startActivity(Intent(this, T::class.java).intentBuilder())
    if (Build.VERSION.SDK_INT >= 34) {
        overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_OPEN,
            android.R.anim.fade_in,
            android.R.anim.fade_out,
        )
    } else {
        @Suppress("DEPRECATION")
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
        )
    }
    if (withFinish) finish()
}

inline fun <reified T: Activity> Activity.resultLauncherWithAnimation(
    intentBuilder: Intent.() -> Intent = { this },
    activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    withFinish: Boolean = true
) {
    activityResultLauncher.launch(
        Intent(this, T::class.java).intentBuilder()
    )
    if (Build.VERSION.SDK_INT >= 34) {
        overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_OPEN,
            android.R.anim.fade_in,
            android.R.anim.fade_out,
        )
    } else {
        @Suppress("DEPRECATION")
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
        )
    }
    if (withFinish) finish()
}