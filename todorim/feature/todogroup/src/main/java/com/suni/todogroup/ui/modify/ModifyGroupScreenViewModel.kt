package com.suni.todogroup.ui.modify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import com.suni.domain.usecase.DeleteGroupDataUseCase
import com.suni.domain.usecase.DeleteTodoDataUseCase
import com.suni.domain.usecase.GetGroupDataUseCase
import com.suni.domain.usecase.GetTodoDataUseCase
import com.suni.domain.usecase.UpdateGroupDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] 그룹 수정 뷰모델
 * 24.09.30 Create class - Q
 */
@HiltViewModel
class ModifyGroupScreenViewModel @Inject constructor(
    private val updateGroupDataUseCase: UpdateGroupDataUseCase,
    private val deleteGroupDataUseCase: DeleteGroupDataUseCase,
    private val getTodoDataUseCase: GetTodoDataUseCase,
    private val deleteTodoDataUseCase: DeleteTodoDataUseCase,
    private val getGroupDataUseCase: GetGroupDataUseCase,
): ViewModel() {

    var state by mutableStateOf(ModifyGroupScreenState())
        private set

    fun onEvent(event: ModifyGroupScreenEvents) {
        when(event) {
            is ModifyGroupScreenEvents.SelectGroupColor -> {
                selectGroupColor(event)
            }
            is ModifyGroupScreenEvents.ModifyGroup -> {
                updateGroup(event)
            }
            is ModifyGroupScreenEvents.DeleteGroup -> {
                deleteGroup(event)
            }
            is ModifyGroupScreenEvents.LoadGroupData -> {
                getGroupData(event)
            }
        }
    }

    private fun getGroupData(event: ModifyGroupScreenEvents.LoadGroupData) {
        viewModelScope.launch {
            getGroupDataUseCase.getGroupById(event.groupId).collectLatest { groupEntity ->
                state = state.copy(
                    groupData = groupEntity,
                    colorIndex = groupEntity.appColorIndex
                )

            }
        }
    }

    /**
     * 그룹 테마 색상 선택
     * @param event
     */
    private fun selectGroupColor(event: ModifyGroupScreenEvents.SelectGroupColor) {
        state = state.copy(
            colorIndex = event.colorIndex
        )
    }

    /**
     * 그룹 정보 변경
     * @param event
     */
    private fun updateGroup(event: ModifyGroupScreenEvents.ModifyGroup) {
        viewModelScope.launch {
            updateGroupDataUseCase(
                GroupEntity().apply {
                    groupId = event.groupId
                    appColorIndex = event.appColorIndex
                    order = event.order
                    title = event.title
                }
            )

            state = state.copy(
                isFinishedModify = true
            )
        }
    }

    /**
     * 그룹 삭제
     * @param event
     */
    private fun deleteGroup(event: ModifyGroupScreenEvents.DeleteGroup) {
        viewModelScope.launch {
            // 그룹 제거
            val targetGroupId = event.groupId
            deleteGroupDataUseCase(targetGroupId)
            // 그룹 하위 할 일 제거
            val todoEntityList = mutableListOf<TodoEntity>()
            getTodoDataUseCase.getTodoByGroupId(targetGroupId).collectLatest { resultTodoList ->
                todoEntityList.addAll(resultTodoList)
            }
            todoEntityList.forEach { todoEntity ->
                deleteTodoDataUseCase(todoEntity.todoId)
            }

            state = state.copy(
                isFinishedDelete = true
            )
        }
    }

}