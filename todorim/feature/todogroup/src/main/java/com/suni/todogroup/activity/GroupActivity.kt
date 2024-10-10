package com.suni.todogroup.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.navigator.KEY_GROUP_FLAG
import com.suni.navigator.GroupScreenFlag
import com.suni.navigator.KEY_GROUP_COLOR_INDEX
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_GROUP_MAX_ID
import com.suni.navigator.KEY_GROUP_MAX_ORDER_ID
import com.suni.navigator.KEY_TODO_FLAG
import com.suni.navigator.KEY_TODO_ID
import com.suni.navigator.KEY_TODO_MAX_ID
import com.suni.navigator.TodoNavigator
import com.suni.navigator.TodoScreenFlag
import com.suni.todogroup.ui.create.CreateGroupScreen
import com.suni.todogroup.ui.create.CreateGroupScreenViewModel
import com.suni.todogroup.ui.detail.GroupDetailScreen
import com.suni.todogroup.ui.detail.GroupDetailScreenViewModel
import com.suni.todogroup.ui.modify.ModifyGroupScreen
import com.suni.todogroup.ui.modify.ModifyGroupScreenViewModel
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * [ComponentActivity] 그룹 (생성/상세)
 * 24.09.11 Create class - Q
 */
@AndroidEntryPoint
class GroupActivity : ComponentActivity(){

    @Inject
    lateinit var todoNavigator: TodoNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val groupScreenFlag =
            intent.getStringExtra(KEY_GROUP_FLAG) ?: GroupScreenFlag.DETAIL.name
        val maxGroupId =
            intent.getIntExtra(KEY_GROUP_MAX_ID, 0)
        val maxOrderId =
            intent.getIntExtra(KEY_GROUP_MAX_ORDER_ID, 0)
        val groupId =
            intent.getIntExtra(KEY_GROUP_ID, 0)

        setContent {
            SuniTodorimTheme {

                val rememberGroupFlag = remember {
                    mutableStateOf(groupScreenFlag)
                }
                val rememberNeedRefresh = remember {
                    mutableStateOf(false)
                }

                when(rememberGroupFlag.value) {
                    GroupScreenFlag.CREATE.name -> {
                        // 그룹 생성
                        val createGroupViewModel = hiltViewModel<CreateGroupScreenViewModel>()
                        CreateGroupScreen(
                            viewModel = createGroupViewModel,
                            maxGroupId = maxGroupId,
                            maxOrderId = maxOrderId,
                        ) {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
                    GroupScreenFlag.DETAIL.name -> {
                        // 그룹 상세
                        val groupDetailViewModel = hiltViewModel<GroupDetailScreenViewModel>()
                        GroupDetailScreen(
                            viewModel = groupDetailViewModel,
                            groupId = groupId,
                            isNeedRefreshHome = rememberNeedRefresh.value,
                            todoNavigatorAction = { launcher, todoMaxId, groupColorIndex ->
                                todoNavigator.containResultNavigateFrom(
                                    activity = this,
                                    withFinish = false,
                                    activityResultLauncher = launcher,
                                    intentBuilder = {
                                        putExtra(KEY_GROUP_ID, groupId)
                                        putExtra(KEY_TODO_MAX_ID, todoMaxId)
                                        putExtra(KEY_GROUP_COLOR_INDEX, groupColorIndex)
                                    }
                                )
                            },
                            refreshHomeScreenAction = {
                                setResult(RESULT_OK)
                                finish()
                            },
                            moveGroupModifyScreenAction = {
                                rememberGroupFlag.value = GroupScreenFlag.MODIFY.name
                            },
                            moveTodoModifyScreenAction = { launcher, todoId, groupColorIndex ->
                                todoNavigator.containResultNavigateFrom(
                                    activity = this,
                                    withFinish = false,
                                    activityResultLauncher = launcher,
                                    intentBuilder = {
                                        putExtra(KEY_TODO_ID, todoId)
                                        putExtra(KEY_GROUP_COLOR_INDEX, groupColorIndex)
                                        putExtra(KEY_TODO_FLAG, TodoScreenFlag.MODIFY.name)
                                    }
                                )
                            },
                        )
                    }
                    GroupScreenFlag.MODIFY.name -> {
                        // 그룹 수정
                        val groupModifyViewModel = hiltViewModel<ModifyGroupScreenViewModel>()
                        ModifyGroupScreen(
                            viewModel = groupModifyViewModel,
                            groupId =groupId,
                            finishedModifyAction = {
                                rememberGroupFlag.value = GroupScreenFlag.DETAIL.name
                                rememberNeedRefresh.value = true
                            },
                            finishedDeleteAction = {
                                setResult(RESULT_OK)
                                finish()
                            }
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier
                            .height(20.dp)
                            .fillMaxHeight())
                    }
                }
            }
        }
    }
}