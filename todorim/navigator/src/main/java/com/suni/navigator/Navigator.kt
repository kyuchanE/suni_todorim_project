package com.suni.navigator

import android.app.Activity
import android.content.Intent

/**
 * [Interface] 네비게이션 인터페이스
 * 24.09.10 Create class - Q
 */
interface Navigator {
    fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent = { this },
        withFinish: Boolean = false,
    )
}

interface MainNavigator: Navigator

interface TodoNavigator: Navigator

interface GroupNavigator: Navigator

const val KEY_GROUP_FLAG = "GROUP_FLAG"
enum class GroupScreenFlag {
    CREATE,
    MODIFY,
    DETAIL,
}