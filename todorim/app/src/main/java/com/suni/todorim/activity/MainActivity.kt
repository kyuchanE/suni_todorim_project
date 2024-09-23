package com.suni.todorim.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.navigator.KEY_GROUP_FLAG
import com.suni.navigator.GroupNavigator
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_GROUP_MAX_ID
import com.suni.navigator.KEY_GROUP_MAX_ORDER_ID
import com.suni.navigator.TodoNavigator
import com.suni.todorim.ui.HomeScreen
import com.suni.todorim.ui.HomeScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * [ComponentActivity] 메인 화면
 * 24.09.02 Create class - Q
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO 메인에서 TodoActivity 접근 시나리오가 있는가?
    @Inject
    lateinit var todoNavigator: TodoNavigator
    @Inject
    lateinit var groupNavigator: GroupNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuniTodorimTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<HomeScreenViewModel>()
                    HomeScreen(
                        viewModel = viewModel,
                        todoNavigatorAction = {
                            todoNavigator.navigateFrom(
                                activity = this,
                                withFinish = false,
                            )
                        },
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