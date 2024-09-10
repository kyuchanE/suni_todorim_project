package com.suni.todorim.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suni.data.model.GroupEntity
import com.suni.domain.findActivity
import com.suni.domain.getDayOfWeek
import com.suni.domain.getStrHomeDate
import com.suni.domain.getTimeNow
import com.suni.ui.R

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    todoNavigatorAction: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.fetchGroupsItem()
    }

    val testBgColor = mutableListOf(
        colorResource(id = R.color.gradient_start_1),
        colorResource(id = R.color.gradient_start_2),
        colorResource(id = R.color.gradient_start_3),
        colorResource(id = R.color.gradient_start_4),
        colorResource(id = R.color.gradient_start_5),
        colorResource(id = R.color.gradient_start_6),
        colorResource(id = R.color.gradient_start_7),
        colorResource(id = R.color.gradient_start_8),
    )

    Scaffold { pv ->
        Surface(
            modifier = Modifier.padding(pv),
            color = testBgColor[viewModel.state.backgroundIndex]
        ) {
            HomeBody(vm = viewModel, todoNavigatorAction = todoNavigatorAction)
        }
        
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeBody(
    vm: HomeScreenViewModel,
    todoNavigatorAction: () -> Unit,
) {

    val pageState = rememberPagerState(
        pageCount = { vm.state.groupLists.size }
    )

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            vm.changeBgColor(
                HomeScreenEvents.ChangeBackground(vm.state.groupLists.lastIndex - page)
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier
            .height(20.dp)
            .fillMaxHeight())
        // 상단 날짜 및 설정 아이콘
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = getStrHomeDate())
            IconButton(onClick = {
                vm.onEvent(HomeScreenEvents.AddGroupItem(
                    groupId = vm.state.maxGroupId + 1,
                    order = vm.state.maxOrder + 1,
                    title = "Number ${vm.state.maxGroupId + 1}!!!",
                    appColorIndex = vm.state.maxGroupId + 1,
                ))
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
            key = { vm.state.groupLists[vm.state.groupLists.lastIndex - it].order },
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 25.dp,
        ) { index ->
            val groupIndex = vm.state.groupLists.lastIndex - index
            val group = vm.state.groupLists[groupIndex]
            GroupContainer(
                item = group,
                groupIndex = groupIndex,
                todoNavigatorAction = todoNavigatorAction
            )
        }
    }
}

@Composable
private fun GroupContainer(
    item: GroupEntity,
    groupIndex: Int,
    todoNavigatorAction: () -> Unit,
) {
    val context = LocalContext.current

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
                    interactionSource = remember{ MutableInteractionSource() },
                    indication = null
                ) {
                    Toast.makeText(context.findActivity(), "Create Group", Toast.LENGTH_SHORT).show()
                }
            ){
                Text(text = "페이지 생성하기!!!")
            }
        } else {
            // 그룹 페이지
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember{ MutableInteractionSource() },
                    indication = null
                ) {
                    Toast.makeText(context.findActivity(), "Group Detail", Toast.LENGTH_SHORT).show()
                    todoNavigatorAction()
                }
            ){
                // Group Title
                Text(text = item.title)
            }
        }

    }

}