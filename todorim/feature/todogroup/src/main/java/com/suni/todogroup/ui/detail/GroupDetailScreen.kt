package com.suni.todogroup.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import com.suni.domain.findActivity
import com.suni.domain.getGradientEndColor
import com.suni.domain.getGradientStartColor
import com.suni.ui.component.GradientFloatingActionButton
import com.suni.ui.component.TdrCheckBox

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailScreenViewModel,
    groupId: Int,
    todoNavigatorAction: (
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
            todoMaxId: Int,
    ) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val groupData: GroupEntity = viewModel.state.groupData
    val groupTodoList: MutableList<TodoEntity> = viewModel.state.todoDataList
    val isChangedInfo = remember {  // 그룹 정보가 변경될 경우
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(groupId))
    }

    val createTodoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.onEvent(GroupDetailScreenEvents.LoadGroupData(groupId))
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
                    isChangedInfo = isChangedInfo.value
                )
                // 할 일 목록
                TodoDataList(modifier = Modifier.fillMaxWidth())
            }
            // 하단 할 일 생성 버튼
            GradientFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                startColor = groupData.appColorIndex.getGradientStartColor(),
                endColor = groupData.appColorIndex.getGradientEndColor(),
                icon = Icons.Filled.Add,
            ) {
                todoNavigatorAction(createTodoLauncher, viewModel.state.todoMaxId)
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
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = groupData.title)

        IconButton(onClick = {
            if (isChangedInfo) {
                // 그룹 정보 변경 >> 메인 데이터 갱신 필요
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
) {
    val test = mutableListOf(
        "테스틑테스트",
        "아침운동",
        "저녁운동",
        "설거지",
        "빨래 돌리기",
        "1",
        "2",
        "3",
        "4",
        "5",
        "ㅁ",
        "ㄱ",
        "ㅎ",
        "ㅅ",
        "ㄱ"
    )
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = test,
            key = { index, str ->
                str
            }
        ) { index, str ->
            TdrCheckBox(
                modifier = Modifier.fillMaxWidth(),
                todoId = index,
                todoTitle = str,
            ) { todoId ->

            }
        }
    }
}