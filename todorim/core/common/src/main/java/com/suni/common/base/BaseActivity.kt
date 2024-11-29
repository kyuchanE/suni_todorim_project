package com.suni.common.base

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.coroutineScope

/**
 * [ComponentActivity] 공통 엑티비티 기능
 * 24.10.29 Create class - Q
 */
abstract class BaseActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}

/**
 * 상단 상태바 / 하단 네비게이션 바 투명
 */
fun Window.fitSystemWindowsWithAdjustResize() {
    setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

    WindowCompat.setDecorFitsSystemWindows(this, true)

    ViewCompat.setOnApplyWindowInsetsListener(decorView) { view, insets ->
        val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

        WindowInsetsCompat
            .Builder()
            .setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(0, 0, 0, bottom)
            )
            .build()
            .apply { ViewCompat.onApplyWindowInsets(view, this) }
    }

    this.statusBarColor = Color.Black.toArgb()
    this.navigationBarColor = Color.Black.toArgb()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
    } else {
        WindowInsetsControllerCompat(this, this.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }


}

fun Activity.hideSystemUI() {

    //Hides the ugly action bar at the top
    actionBar?.hide()

    //Hide the status bars
    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

/**
 * 선택 권한 체크 및 요청
 * @param permissions 권한 목록
 */
fun Activity.checkPermission(
    vararg permissions: String
) {
    // request permission
    val notGrantedPermissions = permissions.filter {
        checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
    }

    if (notGrantedPermissions.isNotEmpty()) {
        requestPermissions(notGrantedPermissions.toTypedArray(), 0)
    }

}

/**
 * 필수 권한 체크 및 요청
 * @param permissions 권한 목록
 * @param onGrantedPermission 권한 허용 시
 * @param onDeniedPermission 권한 거부 시
 */
// TODO chan 작업 필요
suspend fun Activity.forcedCheckPermission(
    vararg permissions: String,
    onGrantedPermission: () -> Unit = {},
    onDeniedPermission: () -> Unit = {}
) {
    coroutineScope {
        // request permission
        val notGrantedPermissions = permissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isNotEmpty()) {
            requestPermissions(notGrantedPermissions.toTypedArray(), 0)
        }

        // check again
        val checkPermissionAgain = permissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (checkPermissionAgain.isNotEmpty()) {
            onDeniedPermission()
        } else {
            onGrantedPermission()
        }
    }

}

fun Window.setMainStatusBarAndNavigationBarColor() {
    // TODO chan 비교 및 각각의 기능 확인 필요
    setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

    WindowCompat.setDecorFitsSystemWindows(this, true)

    this.statusBarColor = Color.Transparent.toArgb()
    this.navigationBarColor = Color.Transparent.toArgb()

    this.decorView.post {
        WindowInsetsControllerCompat(this, this.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }

}

fun Window.setSubStatusBarAndNavigationBarColor() {
    setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

    WindowCompat.setDecorFitsSystemWindows(this, true)

    this.statusBarColor = Color.Transparent.toArgb()
    this.navigationBarColor = Color.Transparent.toArgb()

    this.decorView.post {
        WindowInsetsControllerCompat(this, this.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }

}