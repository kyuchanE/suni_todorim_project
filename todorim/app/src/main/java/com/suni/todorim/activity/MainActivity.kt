package com.suni.todorim.activity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.suni.common.base.BaseActivity
import com.suni.common.base.checkPermission
import com.suni.common.base.forcedCheckPermission
import com.suni.navigator.KEY_GROUP_FLAG
import com.suni.navigator.GroupNavigator
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_GROUP_MAX_ID
import com.suni.navigator.KEY_GROUP_MAX_ORDER_ID
import com.suni.todorim.R
import com.suni.todorim.ui.HomeScreen
import com.suni.todorim.ui.HomeScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ComponentActivity] 메인 화면
 * 24.09.02 Create class - Q
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var groupNavigator: GroupNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.light(
//                Color.Transparent.toArgb(), Color.Transparent.toArgb()
//            ),
//            navigationBarStyle = SystemBarStyle.light(
//                Color.Transparent.toArgb(), Color.Transparent.toArgb()
//            )
//        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel()
        // Notification 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            checkPermission(android.Manifest.permission.POST_NOTIFICATIONS)

        setContent {
            SuniTodorimTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.safeDrawingPadding().fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<HomeScreenViewModel>()
                    HomeScreen(
                        viewModel = viewModel,
                        groupNavigatorAction = { flag, groupId, maxGroupId, maxOrderId, launcher ->
                            groupNavigator.containResultNavigateFrom(
                                activity = this,
                                withFinish = false,
                                activityResultLauncher = launcher,
                                intentBuilder = {
                                    putExtra(KEY_GROUP_FLAG, flag)
                                    putExtra(KEY_GROUP_ID, groupId)
                                    putExtra(KEY_GROUP_MAX_ID, maxGroupId)
                                    putExtra(KEY_GROUP_MAX_ORDER_ID, maxOrderId)
                                }
                            )
                        }
                    )
                }
            }
        }
    }


}

private fun Activity.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(com.suni.common.R.string.notification_channel_name)
        val descriptionText = getString(com.suni.common.R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            getString(com.suni.common.R.string.notification_channel_id),
            name,
            importance
        ).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}