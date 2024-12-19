package com.suni.todogroup.ui.modify

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suni.todogroup.R
import com.suni.todogroup.activity.SharedElementTransition
import com.suni.todogroup.ui.component.GroupBottomButton
import com.suni.todogroup.ui.component.GroupTitle
import com.suni.todogroup.ui.component.SelectGroupColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ModifyGroupScreen(
    viewModel: ModifyGroupScreenViewModel,
    groupId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    finishedModifyAction: () -> Unit = {},
    finishedDeleteAction: () -> Unit = {},
) {
    val context = LocalContext.current

    val strGroupTitle = remember { mutableStateOf("") }
    val currentFocus = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.onEvent(
                ModifyGroupScreenEvents.LoadGroupData(groupId)
            )
            strGroupTitle.value = viewModel.state.groupData.title
        }
    }

    LaunchedEffect(viewModel.state.isFinishedModify, viewModel.state.isFinishedDelete) {
        if (viewModel.state.isFinishedModify)
            finishedModifyAction()
        if (viewModel.state.isFinishedDelete)
            finishedDeleteAction()
    }

    BackHandler {
        if (viewModel.state.isFinishedDelete) {
            finishedDeleteAction()
        } else {
            finishedModifyAction()
        }
    }

    Scaffold { pv ->
        Surface(
            modifier = Modifier
                .padding(pv)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 45.dp)
                    .clickable(
                        interactionSource = remember{ MutableInteractionSource() },
                        indication = null
                    ) {
                        currentFocus.clearFocus()
                    }
            ) {
                // 최상단 타이틀
                GroupTitle(
                    context = context,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    isCreateMode = false,
                    modifyModeCloseAction = {
                        finishedModifyAction()
                    }
                )
                Spacer(modifier = Modifier.height(35.dp))
                // 그룹 색상 선택
                SelectGroupColor(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    strTitle = strGroupTitle,
                    titleChangeEvent = { str ->
                        strGroupTitle.value = str
                    },
                    selectedColorIndex = viewModel.state.colorIndex,
                    selectedColorEvent = { colorIndex: Int ->
                        viewModel.onEvent(
                            ModifyGroupScreenEvents.SelectGroupColor(colorIndex)
                        )
                    }
                )
                with(sharedTransitionScope) {
                    // 그룹 수정/제거 버튼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .height(80.dp)
                            .sharedBounds(
                                rememberSharedContentState(key = SharedElementTransition.KEY_BOTTOM_BUTTON.name),
                                animatedVisibilityScope = animatedVisibilityScope,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                                    contentScale = ContentScale.FillHeight,
                                    alignment = Alignment.CenterEnd
                                )
                            )
                    ) {
                        // 그룹 제거
                        Button(
                            modifier = Modifier
                                .height(65.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(15.dp),
                            contentPadding = PaddingValues(),
                            onClick = {
                                viewModel.onEvent(
                                    ModifyGroupScreenEvents.DeleteGroup(groupId)
                                )
                            },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(
                                        color = colorResource(id = com.suni.ui.R.color.tdr_red),
                                        shape = RoundedCornerShape(15.dp),
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.delete_group),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // 그룹 수정
                        GroupBottomButton(
                            modifier = Modifier
                                .height(65.dp)
                                .weight(1f),
                            selectedColorIndex = viewModel.state.colorIndex,
                            isCreateMode = false,
                            onClickEvent = {
                                viewModel.onEvent(
                                    ModifyGroupScreenEvents.ModifyGroup(
                                        groupId = groupId,
                                        order = viewModel.state.groupData.order,
                                        title = strGroupTitle.value,
                                        appColorIndex = viewModel.state.colorIndex,
                                    )
                                )
                            }
                        )
                    }
                }

            }
        }

    }
}