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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import com.suni.domain.L
import com.suni.domain.findActivity
import com.suni.domain.getDayOfWeek
import com.suni.domain.getStrHomeDate
import com.suni.navigator.GroupScreenFlag
import com.suni.todorim.R
import com.suni.todorim.component.CreateGroupCard
import com.suni.todorim.component.GroupDetailCard
import com.suni.ui.component.LinearGradientProgressIndicator
import com.suni.ui.component.TdrOnlyCheckBox
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
//                .padding(innerPadding)
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
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
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
                ModalBottomContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
    }
}

/**
 * 타이틀
 * @param modifier
 * @param settingOnClickEvent 설정 아이콘 클릭
 */
@Composable
private fun HomeScreenTitle(
    modifier: Modifier,
    settingOnClickEvent: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getStrHomeDate(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        IconButton(onClick = settingOnClickEvent) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "setting",
                tint = Color.White,
            )
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

/**
 * 설정 모달 뷰
 */
@Composable
private fun ModalBottomContainer(
    modifier: Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
        }
        item {
            Text(text = stringResource(id = R.string.str_setting))
            Spacer(modifier = Modifier.height(65.dp))
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