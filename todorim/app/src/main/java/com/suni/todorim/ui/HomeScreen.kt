package com.suni.todorim.ui

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.domain.getDayOfWeek
import com.suni.todorim.component.GroupContainer
import com.suni.todorim.component.HomeScreenTitle
import com.suni.todorim.component.SettingBottomContainer
import com.suni.ui.component.bgGradient
import kotlinx.coroutines.launch

enum class RefreshHomeFlag {
    CREATE_GROUP,
    UPDATE_GROUP,
    NONE,
}

/**
 * 메인 홈 화면
 * @param viewModel
 * @param groupNavigatorAction
 */
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
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

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .background(
                    brush = bgGradient(bgIndex = viewModel.state.backgroundIndex),
                    shape = RectangleShape
                ),
        ) {
            HomeBody(
                vm = viewModel,
                groupNavigatorAction = groupNavigatorAction
            )
        }
    }
}

/**
 * 페이지 리스트
 * @param vm
 * @param groupNavigatorAction
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    vm: HomeScreenViewModel,
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
    var showBottomSheet by remember { mutableStateOf(false) }

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
            .padding(top = 35.dp, bottom = 35.dp)
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
        HomeScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, end = 20.dp),
        ) {
            showBottomSheet = true
        }
        // 요일
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, end = 20.dp),
            text = getDayOfWeek(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
                .fillMaxHeight()
        )
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
            ) { flag ->
                // 페이지 생성 후 페이지 전환 (새로 생상한 페이지로 포커스)
                coroutineScope.launch {
                    val orderIndex = when (flag) {
                        RefreshHomeFlag.UPDATE_GROUP -> vm.getGroupOrder(group.groupId)
                        RefreshHomeFlag.CREATE_GROUP -> vm.getLastGroupOrder()
                        else -> 0
                    }

                    pageState.scrollToPage(orderIndex)
                    vm.changeBgColor(
                        HomeScreenEvents.ChangeBackground(
                            vm.state.groupLists[orderIndex].appColorIndex
                        )
                    )
                }
            }
        }
        // 설정 모달
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.padding(top = 25.dp),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = {
                    showBottomSheet = false
                },
            ) {
                SettingBottomContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
    }
}

/**
 * 홈 스크린 프리 뷰
 */
@Preview
@Composable
internal fun PreviewHomeScreen() {

    Scaffold { pv ->
        Box(
            modifier = Modifier
                .padding(pv)
                .background(
                    brush = bgGradient(bgIndex = 2),
                    shape = RectangleShape
                ),
        ) {

        }
    }
}