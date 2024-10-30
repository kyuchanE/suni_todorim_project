package com.suni.common.base

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import kotlinx.coroutines.coroutineScope

/**
 * [ComponentActivity] 공통 엑티비티 기능
 * 24.10.29 Create class - Q
 */
abstract class BaseActivity: ComponentActivity() {

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