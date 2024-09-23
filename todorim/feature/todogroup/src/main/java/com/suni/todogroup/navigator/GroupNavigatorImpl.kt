package com.suni.todogroup.navigator

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.suni.domain.resultLauncherWithAnimation
import com.suni.domain.startActivityWithAnimation
import com.suni.navigator.GroupNavigator
import com.suni.navigator.Navigator
import com.suni.todogroup.activity.GroupActivity
import javax.inject.Inject

/**
 * [Navigator] 그룹 네비게이션
 * 24.09.11 Create class - Q
 */
class GroupNavigatorImpl @Inject constructor() : GroupNavigator{

    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean
    ) {
        activity.startActivityWithAnimation<GroupActivity>(
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
        activity.resultLauncherWithAnimation<GroupActivity>(
            activityResultLauncher = activityResultLauncher,
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}