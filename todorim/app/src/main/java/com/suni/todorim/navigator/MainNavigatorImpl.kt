package com.suni.todorim.navigator

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.suni.domain.resultLauncherWithFinish
import com.suni.domain.startActivityWithFinish
import com.suni.navigator.MainNavigator
import com.suni.navigator.Navigator
import com.suni.todorim.activity.MainActivity
import javax.inject.Inject

/**
 * [Navigator] 메인 네비게이션
 * 24.09.02 Create class - Q
 */
class MainNavigatorImpl @Inject constructor(): MainNavigator {
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean
    ) {
        activity.startActivityWithFinish<MainActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }

    override fun containResultNavigateFrom(
        activity: Activity,
        activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean
    ) {
        activity.resultLauncherWithFinish<MainActivity>(
            activityResultLauncher = activityResultLauncher,
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}