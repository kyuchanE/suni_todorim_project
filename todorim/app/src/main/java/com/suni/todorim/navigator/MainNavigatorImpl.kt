package com.suni.todorim.navigator

import android.app.Activity
import android.content.Intent
import com.suni.domain.startActivityWithAnimation
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
        activity.startActivityWithAnimation<MainActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}