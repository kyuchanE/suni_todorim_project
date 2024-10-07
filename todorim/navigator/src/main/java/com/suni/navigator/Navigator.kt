package com.suni.navigator

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

/**
 * [Interface] 네비게이션 인터페이스
 * 24.09.10 Create class - Q
 */
interface Navigator {
    fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent = { this },
        withFinish: Boolean = true,
    )

    fun containResultNavigateFrom(
        activity: Activity,
        activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean = true,
    )
}

interface MainNavigator: Navigator

interface TodoNavigator: Navigator

interface GroupNavigator: Navigator

const val KEY_GROUP_FLAG = "GROUP_FLAG"
const val KEY_GROUP_ID = "GROUP_ID"
const val KEY_GROUP_MAX_ID = "GROUP_MAX_ID"
const val KEY_GROUP_COLOR_INDEX = "KEY_GROUP_COLOR_INDEX"
const val KEY_GROUP_MAX_ORDER_ID = "GROUP_MAX_ORDER_ID"
const val KEY_TODO_MAX_ID = "TODO_MAX_ID"
enum class GroupScreenFlag {
    CREATE,
    MODIFY,
    DETAIL,
}
const val KEY_TODO_FLAG = "TODO_FLAG"
const val KEY_TODO_ID = "TODO_ID"
enum class TodoScreenFlag {
    CREATE,
    MODIFY,
}
