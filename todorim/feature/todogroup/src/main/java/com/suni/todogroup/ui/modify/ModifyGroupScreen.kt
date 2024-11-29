package com.suni.todogroup.ui.modify

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.suni.todogroup.ui.component.GroupBottomButton
import com.suni.todogroup.ui.component.GroupTitle
import com.suni.todogroup.ui.component.SelectGroupColor
import kotlinx.coroutines.launch

@Composable
fun ModifyGroupScreen(
    viewModel: ModifyGroupScreenViewModel,
    groupId: Int,
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
                        .fillMaxWidth(),
                    isCreateMode = false,

                )
                Spacer(modifier = Modifier.height(35.dp))
                // 그룹 색상 선택
                SelectGroupColor(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
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
                // 그룹 수정/제거 버튼
                GroupBottomButton(
                    modifier = Modifier.fillMaxWidth(),
                    selectedColorIndex = viewModel.state.colorIndex,
                    isCreateMode = false,
                    onClickDeleteEvent = {
                        viewModel.onEvent(
                            ModifyGroupScreenEvents.DeleteGroup(groupId)
                        )
                    },
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