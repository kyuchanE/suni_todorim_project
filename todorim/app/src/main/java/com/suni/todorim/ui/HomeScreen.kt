package com.suni.todorim.ui

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.data.model.GroupEntity
import com.suni.domain.findActivity
import com.suni.domain.getDayOfWeek
import com.suni.domain.getStrHomeDate
import com.suni.navigator.GroupScreenFlag
import com.suni.ui.component.bgGradient
import kotlinx.coroutines.launch

/**
 * 메인 홈 화면
 * @param viewModel
 * @param todoNavigatorAction
 * @param groupNavigatorAction
 */
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    todoNavigatorAction: () -> Unit = {},
    groupNavigatorAction: (
        groupFlag: String,
        groupId: Int,
        maxGroupId: Int,
        maxOrderId: Int,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    ) -> Unit = { _, _, _, _, _ -> },
) {

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeScreenEvents.LoadLocalData)
    }

    Scaffold { pv ->
        Box(
            modifier = Modifier
                .padding(pv)
                .background(
                    brush = bgGradient(bgIndex = viewModel.state.backgroundIndex),
                    shape = RectangleShape
                ),
        ) {
            HomeBody(
                vm = viewModel,
                todoNavigatorAction = todoNavigatorAction,
                groupNavigatorAction = groupNavigatorAction
            )
        }
    }
}

/**
 * 페이지 리스트
 * @param vm
 * @param todoNavigatorAction
 * @param groupNavigatorAction
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeBody(
    vm: HomeScreenViewModel,
    todoNavigatorAction: () -> Unit = {},
    groupNavigatorAction: (
        groupFlag: String,
        groupId: Int,
        maxGroupId: Int,
        maxOrderId: Int,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    ) -> Unit = { _, _, _, _, _ -> },
) {
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState(
        pageCount = { vm.state.groupLists.size }
    )

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            vm.changeBgColor(
                HomeScreenEvents.ChangeBackground(
                    if (page == pageState.pageCount - 1) {
                        // 페이지 생성
                        0
                    } else {
                        vm.state.groupLists[page].appColorIndex
                    }
                )
            )

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .height(20.dp)
                .fillMaxHeight()
        )
        // 상단 날짜 및 설정 아이콘
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = getStrHomeDate())
            IconButton(onClick = {
                vm.onEvent(
                    HomeScreenEvents.AddGroupItem(
                        groupId = vm.state.maxGroupId + 1,
                        order = vm.state.maxOrder + 1,
                        title = "Number ${vm.state.maxGroupId + 1}!!!",
                        appColorIndex = vm.state.maxGroupId + 1,
                    )
                )
            }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
            }
        }
        Text(text = getDayOfWeek())
        // 할일 목록
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            key = { vm.state.groupLists[it].groupId },
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 25.dp,
        ) { index ->
            val group = vm.state.groupLists[index]
            GroupContainer(
                vm = vm,
                item = group,
                groupIndex = group.groupId,
                maxGroupId = vm.state.maxGroupId,
                maxOrderId = vm.state.maxOrder,
                groupNavigatorAction = groupNavigatorAction,
            ) {
                // 페이지 생성 후 페이지 전환 (새로 생상한 페이지로 포커스)
                coroutineScope.launch {
                    pageState.scrollToPage(pageState.pageCount - 2)
                    vm.changeBgColor(
                        HomeScreenEvents.ChangeBackground(
                            vm.state.groupLists[pageState.pageCount - 2].appColorIndex
                        )
                    )
                }
            }
        }
    }
}

/**
 * 페이지 리스트 아이템
 * @param item
 * @param groupIndex
 * @param groupNavigatorAction
 */
@Composable
private fun GroupContainer(
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
    refreshNewPage: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var refreshFlag = remember {
        mutableStateOf(false)
    }

    val createGroupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshFlag.value = true
        }
    }

    val detailGroupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshFlag.value = true
        }
    }

    LaunchedEffect(refreshFlag.value) {
        if (refreshFlag.value) {
            coroutineScope.launch {
                refreshFlag.value = false
                vm.onEvent(HomeScreenEvents.LoadLocalData)
                refreshNewPage()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color.White
    ) {
        if (groupIndex == 0) {
            // 새로운 그룹 생성 페이지
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    Toast.makeText(context.findActivity(), "Create Group", Toast.LENGTH_SHORT)
                        .show()
                    
                    groupNavigatorAction(
                        GroupScreenFlag.CREATE.name,
                        groupIndex,
                        maxGroupId,
                        maxOrderId,
                        createGroupLauncher,
                    )
                }
            ) {
                Text(text = "페이지 생성하기!!!")
            }
        } else {
            // 그룹 페이지
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    Toast.makeText(context.findActivity(), "Group Detail", Toast.LENGTH_SHORT)
                        .show()
                    groupNavigatorAction(
                        GroupScreenFlag.DETAIL.name,
                        groupIndex,
                        maxGroupId,
                        maxOrderId,
                        detailGroupLauncher,
                    )
                }
            ) {
                // Group Title
                Text(text = item.title)
            }
        }

    }

}