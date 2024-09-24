package com.suni.todorim.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.suni.data.model.GroupEntity
import com.suni.domain.usecase.GetGroupDataUseCase
import com.suni.domain.usecase.WriteGroupDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] 메인 뷰모델
 * 24.09.02 Create class - Q
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getGroupDataUseCase: GetGroupDataUseCase,
    private val writeGroupDataUseCase: WriteGroupDataUseCase,
): ViewModel() {

    var state by mutableStateOf(HomeScreenState())
        private set

    fun onEvent(event: HomeScreenEvents) {
        when(event) {
            is HomeScreenEvents.AddGroupItem -> {
                addGroupItem(event)
            }
            is HomeScreenEvents.AddTodoItem -> {

            }
            is HomeScreenEvents.LoadLocalData -> {
                fetchGroupsItem()
            }
            is HomeScreenEvents.ChangeBackground -> {
                changeBgColor(event)
            }
        }
    }

    /**
     * 그룹 정보 저장
     * @param event AddGroupItem
     */
    private fun addGroupItem(
        event: HomeScreenEvents.AddGroupItem,
        needFetch: Boolean = true,
    ) {
        writeGroupDataUseCase(
            GroupEntity().apply {
                groupId = event.groupId
                order = event.order
                title = event.title
                startColorHex = event.startColorHex
                endColorHex = event.endColorHex
                appColorIndex = event.appColorIndex
            }
        )

        if (needFetch)
            fetchGroupsItem()
    }

    /**
     * 모든 그룹 정보 조회
     */
    private fun fetchGroupsItem() {
        val groupList = mutableListOf<GroupEntity>()
        val realmResult = getGroupDataUseCase.getGroupsAll()
        var maxGroupId = 0
        var maxOrder = 0

        if (realmResult.isEmpty()) {
            addGroupItem(
                HomeScreenEvents.AddGroupItem(
                    groupId = 1,
                    order = 1,
                    title = "Default",
                    appColorIndex = 0,
                )
            )
        } else {
            realmResult.forEach { groupData ->
                groupList.add(groupData)
                if (maxGroupId < groupData.groupId)
                    maxGroupId = groupData.groupId
                if (maxOrder < groupData.order)
                    maxOrder = groupData.order
            }

            groupList.sortBy { it.groupId }
            // Default 페이지 생성 아이템 추가
            groupList.add(
                GroupEntity()
            )

            state = state.copy(
                groupLists = groupList,
                maxGroupId = maxGroupId,
                maxOrder = maxOrder,
            )
        }
    }

    fun changeBgColor(event: HomeScreenEvents.ChangeBackground) {
        state = state.copy(
            backgroundIndex = event.bgIndex
        )
    }

}