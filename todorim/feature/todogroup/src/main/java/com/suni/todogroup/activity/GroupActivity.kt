package com.suni.todogroup.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.common.base.BaseActivity
import com.suni.common.base.setSubStatusBarAndNavigationBarColor
import com.suni.navigator.KEY_GROUP_FLAG
import com.suni.navigator.GroupScreenFlag
import com.suni.navigator.KEY_GROUP_ID
import com.suni.navigator.KEY_GROUP_MAX_ID
import com.suni.navigator.KEY_GROUP_MAX_ORDER_ID
import com.suni.navigator.TodoNavigator
import com.suni.todogroup.ui.create.CreateGroupScreen
import com.suni.todogroup.ui.create.CreateGroupScreenViewModel
import com.suni.todogroup.ui.detail.GroupDetailScreen
import com.suni.todogroup.ui.detail.GroupDetailScreenViewModel
import com.suni.todogroup.ui.modify.ModifyGroupScreen
import com.suni.todogroup.ui.modify.ModifyGroupScreenViewModel
import com.suni.todogroup.ui.todo.create.CreateTodoScreenViewModel
import com.suni.todogroup.ui.todo.create.CreateTodoScreen
import com.suni.todogroup.ui.todo.modify.ModifyTodoScreenViewModel
import com.suni.todogroup.ui.todo.modify.ModifyTodoScreen
import com.suni.ui.theme.SuniTodorimTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

enum class SharedElementTransition {
    KEY_BACKGROUND,
    KEY_BOTTOM_BUTTON,
}

/**
 * [ComponentActivity] 그룹 (생성/상세)
 * 24.09.11 Create class - Q
 */
@AndroidEntryPoint
class GroupActivity : BaseActivity(){

    @Inject
    lateinit var todoNavigator: TodoNavigator

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSubStatusBarAndNavigationBarColor()

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
                val rememberMaxTodoId = remember {
                    mutableIntStateOf(0)
                }
                val rememberColorIndex = remember {
                    mutableIntStateOf(0)
                }
                val rememberTodoId = remember {
                    mutableIntStateOf(0)
                }
                val rememberBackStack = remember {
                    mutableStateOf("")
                }

                SharedTransitionLayout {
                    AnimatedContent(
                        targetState = rememberGroupFlag.value,
                        label = "GroupScreen"
                    ) { targetState ->
                        when(targetState) {
                            GroupScreenFlag.CREATE.name -> {
                                // 그룹 생성
                                rememberBackStack.value = GroupScreenFlag.CREATE.name
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
                                rememberBackStack.value = GroupScreenFlag.DETAIL.name
                                val groupDetailViewModel = hiltViewModel<GroupDetailScreenViewModel>()
                                GroupDetailScreen(
                                    viewModel = groupDetailViewModel,
                                    groupId = groupId,
                                    isNeedRefreshHome = rememberNeedRefresh.value,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                    moveCreateTodoScreenAction = { todoMaxId, groupColorIndex ->
                                        rememberMaxTodoId.intValue = todoMaxId
                                        rememberColorIndex.intValue = groupColorIndex

                                        rememberGroupFlag.value = GroupScreenFlag.TODO_CREATE.name
                                    },
                                    refreshHomeScreenAction = {
                                        setResult(RESULT_OK)
                                        finish()
                                    },
                                    moveGroupModifyScreenAction = {
                                        rememberGroupFlag.value = GroupScreenFlag.MODIFY.name
                                    },
                                    moveTodoModifyScreenAction = { todoId, groupColorIndex ->
                                        rememberColorIndex.intValue = groupColorIndex
                                        rememberTodoId.intValue = todoId

                                        rememberGroupFlag.value = GroupScreenFlag.TODO_MODIFY.name
                                    },
                                )
                                rememberNeedRefresh.value = false
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
                            GroupScreenFlag.TODO_CREATE.name -> {
                                // 할 일 생성
                                val todoCreateViewModel = hiltViewModel<CreateTodoScreenViewModel>()
                                CreateTodoScreen(
                                    viewModel = todoCreateViewModel,
                                    groupId = groupId,
                                    groupColorIndex = rememberColorIndex.intValue,
                                    todoMaxId = rememberMaxTodoId.intValue,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                    finishCreateTodoScreen = { isNeedRefresh ->
                                        rememberNeedRefresh.value = isNeedRefresh
                                        rememberGroupFlag.value = rememberBackStack.value
                                    }
                                )
                            }
                            GroupScreenFlag.TODO_MODIFY.name -> {
                                // 할 일 수정
                                val todoModifyViewModel = hiltViewModel<ModifyTodoScreenViewModel>()
                                ModifyTodoScreen(
                                    viewModel = todoModifyViewModel,
                                    todoId = groupId,
                                    groupColorIndex = rememberColorIndex.intValue,
                                    finishModifyTodoScreen = { isNeedRefresh ->
                                        rememberNeedRefresh.value = isNeedRefresh
                                        rememberGroupFlag.value = rememberBackStack.value
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
    }
}