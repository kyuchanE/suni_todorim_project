package com.suni.todorim.component

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.suni.data.model.GroupEntity
import com.suni.navigator.GroupScreenFlag
import com.suni.todorim.ui.HomeScreenEvents
import com.suni.todorim.ui.HomeScreenViewModel
import com.suni.todorim.ui.RefreshHomeFlag
import kotlinx.coroutines.launch

/**
 * 페이지 리스트 아이템
 * @param vm : HomeScreenViewModel
 * @param item : GroupEntity
 * @param groupIndex
 * @param groupNavigatorAction 네비게이션 페이지 이동 액셕 (그룹 상세 / 그룹 생성)
 * @param refreshNewPage 새로고침 이벤트
 */
@Composable
fun GroupContainer(
    vm: HomeScreenViewModel,
    item: GroupEntity,
    groupIndex: Int,
    maxGroupId: Int,
    maxOrderId: Int,
    groupNavigatorAction: (
        groupFlag: String,
        groupId: Int,
        maxGroupId: Int,
        maxOrderId: Int,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    ) -> Unit = { _, _, _, _, _ -> },
    refreshNewPage: (flag: RefreshHomeFlag) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val refreshFlag = remember {
        mutableStateOf(RefreshHomeFlag.NONE)
    }

    val createGroupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshFlag.value = RefreshHomeFlag.CREATE_GROUP
        }
    }

    val detailGroupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshFlag.value = RefreshHomeFlag.UPDATE_GROUP
        }
    }

    LaunchedEffect(refreshFlag.value) {
        if (refreshFlag.value != RefreshHomeFlag.NONE) {
            coroutineScope.launch {
                vm.onEvent(HomeScreenEvents.LoadLocalData)
                refreshNewPage(refreshFlag.value)
                refreshFlag.value = RefreshHomeFlag.NONE
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 35.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
    ) {
        if (groupIndex == 0) {
            // 새로운 그룹 생성 페이지
            CreateGroupCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(25.dp),
                onClick = {
                    groupNavigatorAction(
                        GroupScreenFlag.CREATE.name,
                        groupIndex,
                        maxGroupId,
                        maxOrderId,
                        createGroupLauncher,
                    )
                }
            )
        } else {
            // 그룹 페이지
            GroupDetailCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(25.dp),
                title = item.title,
                colorIndex = item.appColorIndex,
                todoCompletedPercent = vm.getTodoCompletedPercent(item.groupId),
                todoItemList = vm.state.todoLists
                    .filter { it.groupId == item.groupId }
                    .filter { !it.isCompleted }
                    .toMutableList(),
                onClickDetailCard = {
                    groupNavigatorAction(
                        GroupScreenFlag.DETAIL.name,
                        groupIndex,
                        maxGroupId,
                        maxOrderId,
                        detailGroupLauncher,
                    )
                },
                onClickCheckBox = { resultItem ->
                    vm.onEvent(HomeScreenEvents.UpdateTodoData(resultItem))
                }
            )
        }
    }

}