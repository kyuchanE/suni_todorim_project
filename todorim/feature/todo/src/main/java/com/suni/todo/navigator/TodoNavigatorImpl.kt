package com.suni.todo.navigator

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.suni.domain.resultLauncherWithAnimation
import com.suni.domain.startActivityWithAnimation
import com.suni.navigator.Navigator
import com.suni.navigator.TodoNavigator
import com.suni.todo.activity.TodoActivity
import javax.inject.Inject

/**
 * [Navigator] 할일 네비게이션
 * 24.09.02 Create class - Q
 */
class TodoNavigatorImpl @Inject constructor() : TodoNavigator{
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean
    ) {
        activity.startActivityWithAnimation<TodoActivity>(
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
        activity.resultLauncherWithAnimation<TodoActivity>(
            activityResultLauncher = activityResultLauncher,
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }

}