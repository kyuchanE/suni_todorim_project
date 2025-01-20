package com.suni.todorim.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import com.suni.domain.usecase.GetGroupDataUseCase
import com.suni.domain.usecase.GetTodoDataUseCase
import com.suni.domain.usecase.UpdateTodoDataUseCase
import com.suni.domain.usecase.WriteGroupDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] 메인 뷰모델
 * 24.09.02 Create class - Q
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getGroupDataUseCase: GetGroupDataUseCase,
    private val writeGroupDataUseCase: WriteGroupDataUseCase,
    private val getTodoDataUseCase: GetTodoDataUseCase,
    private val updateTodoUseCase: UpdateTodoDataUseCase,
): ViewModel() {

    var state by mutableStateOf(HomeScreenState())
        private set

    fun onEvent(event: HomeScreenEvents) {
        when(event) {
            is HomeScreenEvents.AddGroupItem -> {
                addGroupItem(event)
            }
            is HomeScreenEvents.LoadLocalData -> {
                fetchAll()
            }
            is HomeScreenEvents.ChangeBackground -> {
                changeBgColor(event)
            }
            is HomeScreenEvents.UpdateTodoData -> {
                updateTodoCompleted(event)
            }
        }
    }

    /**
     * 할 일 완료 처리
     */
    private fun updateTodoCompleted(event: HomeScreenEvents.UpdateTodoData) {
        viewModelScope.launch {
            updateTodoUseCase(event.todoEntity)
            fetchTodoData()
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
            fetchAll()
    }

    /**
     * 모든 그룹 정보 조회 + 모든 할 일 정보 조회
     */
    private fun fetchAll() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            fetchGroupData().join()
            fetchTodoData().join()
        }

    }

    /**
     * 모든 할 일 정보 조회
     */
    private fun fetchTodoData() : Job = viewModelScope.launch {
        val todoList = mutableListOf<TodoEntity>()

        getTodoDataUseCase.getTodoAll().collectLatest { todoEntityList ->
            todoEntityList.forEach { todoEntity ->
                todoList.add(todoEntity)
            }
        }

        state = state.copy(
            isLoading = false,
            todoLists = todoList
        )
    }

    /**
     * 모든 그룹 정보 조회 JOB
     */
    private fun fetchGroupData() : Job = viewModelScope.launch {
        val groupList = mutableListOf<GroupEntity>()
        var maxGroupId = 0
        var maxOrder = 0

        getGroupDataUseCase.getGroupsAll().collectLatest { resultList ->
            if (resultList.isEmpty()) {
                addGroupItem(
                    HomeScreenEvents.AddGroupItem(
                        groupId = 1,
                        order = 1,
                        title = "Default",
                        appColorIndex = 0,
                    )
                )
            } else {
                resultList.forEach { groupEntity ->
                    groupList.add(groupEntity)
                    if (maxGroupId < groupEntity.groupId)
                        maxGroupId = groupEntity.groupId
                    if (maxOrder < groupEntity.order)
                        maxOrder = groupEntity.order
                }

                groupList.sortBy { it.groupId }
                // Default 페이지 생성 아이템 추가
                groupList.add(
                    GroupEntity()
                )

                state = state.copy(
                    groupLists = groupList,
                    maxGroupId = maxGroupId + 1,
                    maxOrder = maxOrder + 1,
                )
            }
        }

    }

    fun changeBgColor(event: HomeScreenEvents.ChangeBackground) {
        state = state.copy(
            backgroundIndex = event.bgIndex
        )
    }

    /**
     * 할 일 진행률
     */
    fun getTodoCompletedPercent(groupId: Int): Float {
        var total = 0
        var completed = 0
        state.todoLists.forEach {
            if (it.groupId == groupId) {
                total++
                if (it.isCompleted)
                    completed++
            }
        }

        return if (total == 0 || completed == 0) {
            0f
        } else {
            completed.toFloat() / total.toFloat() * 100
        }
    }

    /**
     * 해당 그룹 순서 반환
     * @param groupId
     */
    fun getGroupOrder(groupId: Int): Int {
        var returnOrder = 0
        run loop@ {
            state.groupLists.forEachIndexed { index, groupEntity ->
                if (groupEntity.groupId == groupId) {
                    returnOrder = index
                    return@loop
                }
            }
        }
        return returnOrder
    }

    fun getLastGroupOrder(): Int {
        return state.groupLists.lastIndex - 1
    }

}