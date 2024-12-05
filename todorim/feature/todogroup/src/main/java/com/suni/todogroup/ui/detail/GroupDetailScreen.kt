package com.suni.todogroup.ui.detail

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import com.suni.domain.findActivity
import com.suni.domain.getGradientEndColor
import com.suni.domain.getGradientStartColor
import com.suni.todogroup.activity.SharedElementTransition
import com.suni.ui.R
import com.suni.ui.component.GradientFloatingActionButton
import com.suni.ui.component.LinearGradientProgressIndicator
import com.suni.ui.component.TdrCheckBox

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailScreenViewModel,
    groupId: Int,
    isNeedRefreshHome: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    moveCreateTodoScreenAction: (
        todoMaxId: Int,
        groupColorIndex: Int,
    ) -> Unit = { _, _ -> },
    moveGroupModifyScreenAction: () -> Unit = {},
    moveTodoModifyScreenAction: (
        todoId: Int,
        groupColorIndex: Int,
    ) -> Unit = { _, _ -> },
    refreshHomeScreenAction: () -> Unit = {},
) {
    val context = LocalContext.current
    val groupData: GroupEntity = viewModel.state.groupData
    val groupTodoList: MutableList<TodoEntity> = viewModel.state.todoDataList
    val isChangedInfo = viewModel.state.isNeedRefreshHome

    LaunchedEffect(Unit) {
        if (isNeedRefreshHome)
            viewModel.setNeedRefreshState(true)
        viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(groupId))
    }

    // 시스템 백키
    BackHandler {
        if (isChangedInfo) {
            refreshHomeScreenAction()
        } else {
            context.findActivity().finish()
        }
    }

    Scaffold { pv ->
        Box(
            modifier = Modifier
                .padding(pv)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = Color.White),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 45.dp)
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // 최상단 타이틀
                GroupDetailTitle(
                    context = context,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    groupData = groupData,
                    isChangedInfo = isChangedInfo,
                    refreshHomeScreenAction = refreshHomeScreenAction,
                    moveGroupModifyScreenAction = moveGroupModifyScreenAction,
                )
                Spacer(modifier = Modifier.height(10.dp))
                // 할 일 진행률
                LinearGradientProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .padding(horizontal = 8.dp),
                    backgroundColor = Color.LightGray,
                    colorIndex = viewModel.state.groupData.appColorIndex,
                    percent = viewModel.getTodoCompletedPercent()
                )
                Spacer(modifier = Modifier.height(15.dp))
                // 할 일 목록
                TodoDataList(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                    viewModel = viewModel,
                    groupTodoList = groupTodoList,
                    moveTodoModifyScreenAction = { todoId ->
                        moveTodoModifyScreenAction(
                            todoId,
                            viewModel.state.groupData.appColorIndex,
                        )
                    }
                )
            }
            // 하단 할 일 생성 버튼
            GradientFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 35.dp, vertical = 55.dp),
                startColor = groupData.appColorIndex.getGradientStartColor(),
                endColor = groupData.appColorIndex.getGradientEndColor(),
                icon = Icons.Filled.Add,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                keySharedElement = SharedElementTransition.KEY_BOTTOM_BUTTON.name,
            ) {
                moveCreateTodoScreenAction(
                    viewModel.state.todoMaxId,
                    viewModel.state.groupData.appColorIndex,
                )
            }

        }
    }
}

/**
 * 최상단 타이틀
 * @param context
 * @param modifier
 * @param groupData
 * @param isChangedInfo
 */
@Composable
private fun GroupDetailTitle(
    context: Context,
    modifier: Modifier,
    groupData: GroupEntity,
    isChangedInfo: Boolean,
    refreshHomeScreenAction: () -> Unit = { },
    moveGroupModifyScreenAction: () -> Unit = { },
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 그룹 Title
        Text(
            modifier = Modifier.weight(1f),
            text = groupData.title,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(4.dp))
        // 그룹 수정
        IconButton(
            modifier = Modifier.size(45.dp),
            onClick = moveGroupModifyScreenAction,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_modify_dark),
                contentDescription = "Modify Group",
            )
        }
        // 닫기
        IconButton(onClick = {
            if (isChangedInfo) {
                // 그룹 정보 변경 >> 메인 데이터 갱신 필요
                refreshHomeScreenAction()
            } else {
                context.findActivity().finish()
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
            )
        }
    }
}

/**
 * 그룹 할 일 리스트
 */
@Composable
private fun TodoDataList(
    modifier: Modifier,
    viewModel: GroupDetailScreenViewModel,
    groupTodoList: MutableList<TodoEntity>,
    moveTodoModifyScreenAction: (todoId: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = groupTodoList,
            key = { _, todoData ->
                todoData.todoId
            }
        ) { _, todoData ->
            // 할 일 컨텐츠
            TodoItemComponent(
                modifier = Modifier.fillMaxSize(),
                todoData = todoData,
                swipeToDelete = { todoEntity ->
                    viewModel.onEvent(
                        GroupDetailScreenEvents.DeleteTodoData(todoEntity.todoId) {
                            viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(todoEntity.groupId, true))
                        }
                    )
                },
                swipeToEnter = { todoEntity ->
                    moveTodoModifyScreenAction(todoEntity.todoId)
                },
                onChangedComplete = { todoEntity ->
                    viewModel.onEvent(
                        GroupDetailScreenEvents.UpdateTodoData(todoEntity) {
                            viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(todoEntity.groupId, true))
                        }
                    )
                }
            )
        }

    }
}

/**
 * 할 일 (스와이프 좌 우)
 */
@Composable
fun TodoItemComponent(
    modifier: Modifier,
    todoData: TodoEntity,
    swipeToDelete: (data: TodoEntity) -> Unit = { _ -> },
    swipeToEnter: (data: TodoEntity) -> Unit = { _ -> },
    onChangedComplete: (data: TodoEntity) -> Unit = { _ -> },
) {
    val currentItem by rememberUpdatedState(newValue = todoData)
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.Settled -> {
                    false
                }

                SwipeToDismissBoxValue.StartToEnd -> { // -> 방향 스와이프
                    swipeToEnter(currentItem)
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> { // <- 방향 스와이프
                    swipeToDelete(currentItem)
                    false
                }
            }
        },
        positionalThreshold = { it * 0.2f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            // 스와이프 할 때 보여지는 컨텐츠
            val direction = dismissState.dismissDirection
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.White.copy(alpha = 0.5f)
                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.4f)
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green.copy(alpha = 0.5f)
                },
                label = "ColorAnimation",
            )
            val icon = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.Settled -> painterResource(id = R.drawable.baseline_circle_24)
                SwipeToDismissBoxValue.EndToStart -> painterResource(id = R.drawable.ic_remove_white)
                SwipeToDismissBoxValue.StartToEnd -> painterResource(id = R.drawable.ic_modify_white)
            }
            val scale by animateFloatAsState(
                targetValue = when (dismissState.targetValue == SwipeToDismissBoxValue.Settled) {
                    true -> 0.3f
                    else -> 1.0f
                },
                label = "FloatAnimation",
            )
            val alignment = when (direction) {
                SwipeToDismissBoxValue.Settled -> Alignment.CenterEnd
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 30.dp),
                contentAlignment = alignment
            ) {
                val iconTintColor =
                    if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) Color.LightGray
                    else Color.White
                Icon(
                    modifier = Modifier
                        .scale(scale)
                        .size(25.dp),
                    painter = icon,
                    tint = iconTintColor,
                    contentDescription = null
                )
            }
        },
        content = {
            // 스와이프 될 컨텐츠
            TdrCheckBox(
                modifier = Modifier.fillMaxWidth(),
                todoTitle = currentItem.title,
                isSelected = currentItem.isCompleted,
            ) { isSelected ->
                val resultItem = TodoEntity().apply {
                    todoId = currentItem.todoId
                    groupId = currentItem.groupId
                    title = currentItem.title
                    order = currentItem.order
                    isCompleted = isSelected
                }
                onChangedComplete(resultItem)
            }
        }
    )
}