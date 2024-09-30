package com.suni.todogroup.ui.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.suni.todogroup.ui.component.GroupBottomButton
import com.suni.todogroup.ui.component.GroupTitle
import com.suni.todogroup.ui.component.SelectGroupColor

/**
 * 그룹 생성 화면
 * @param viewModel
 * @param finishedCreateAction
 */
@Composable
fun CreateGroupScreen(
    viewModel: CreateGroupScreenViewModel,
    maxGroupId: Int = 0,
    maxOrderId: Int = 0,
    finishedCreateAction: () -> Unit = { },
) {
    val context = LocalContext.current

    val strGroupTitle = remember { mutableStateOf("") }
    val currentFocus = LocalFocusManager.current

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

                )
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
                            CreateGroupScreenEvents.SelectGroupColor(colorIndex)
                        )
                    }
                )
                // 그룹 생성 버튼
                GroupBottomButton(
                    modifier = Modifier.fillMaxWidth(),
                    selectedColorIndex = viewModel.state.colorIndex,
                    onClickEvent = {
                        viewModel.onEvent(
                            CreateGroupScreenEvents.CreateGroup(
                                groupId = maxGroupId,
                                order = maxOrderId,
                                title = strGroupTitle.value,
                                appColorIndex = viewModel.state.colorIndex,
                            )
                        )
                        finishedCreateAction()
                    }
                )
            }
        }

    }
}


