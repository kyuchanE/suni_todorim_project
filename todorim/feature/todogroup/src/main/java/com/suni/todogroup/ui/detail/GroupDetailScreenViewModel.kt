package com.suni.todogroup.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.TodoEntity
import com.suni.domain.usecase.DeleteTodoDataUseCase
import com.suni.domain.usecase.GetGroupDataUseCase
import com.suni.domain.usecase.GetTodoDataUseCase
import com.suni.domain.usecase.UpdateTodoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] 그룹 상세 뷰모델
 * 24.09.23 Create class - Q
 */
@HiltViewModel
class GroupDetailScreenViewModel @Inject constructor(
    private val getGroupDataUseCase: GetGroupDataUseCase,
    private val getTodoDataUseCase: GetTodoDataUseCase,
    private val updateTodoDataUseCase: UpdateTodoDataUseCase,
    private val deleteTodoDataUseCase: DeleteTodoDataUseCase,
): ViewModel() {

    var state by mutableStateOf(GroupDetailScreenState())
        private set

    fun onEvent(event: GroupDetailScreenEvents) {
        when(event) {
            is GroupDetailScreenEvents.LoadGroupData -> {
                fetchGroupItem(event)
            }
            is GroupDetailScreenEvents.UpdateTodoData -> {
                updateTodoCompleted(event)
            }
            is GroupDetailScreenEvents.DeleteTodoData -> {
                deleteTodoEntity(event)
            }
        }
    }

    /**
     * 할 일 삭제
     * @param event
     */
    private fun deleteTodoEntity(
        event: GroupDetailScreenEvents.DeleteTodoData,
    ) {
        viewModelScope.launch {
            deleteTodoDataUseCase(event.todoId)
            event.finishedEvent()
        }
    }

    /**
     * 할 일 완료 수정
     * @param event
     */
    private fun updateTodoCompleted(event: GroupDetailScreenEvents.UpdateTodoData) {
        viewModelScope.launch {
            updateTodoDataUseCase(event.todoEntity)
        }
    }

    /**
     * 특정 그룹 데이터 조회
     * @param event
     */
    private fun fetchGroupItem(event: GroupDetailScreenEvents.LoadGroupData) {
        viewModelScope.launch {
            // fetch Group
            val resultGroupItem = getGroupDataUseCase.getGroupById(event.groupId)
            if (resultGroupItem.isNotEmpty()) {
                state = state.copy(
                    groupData = resultGroupItem.first()
                )
            }
            // fetch Group TodoList
            var todoMaxId = -1
            val resultTodoItems = getTodoDataUseCase.getTodoByGroupId(event.groupId)
            if (resultTodoItems.isNotEmpty()) {
                val resultList = mutableListOf<TodoEntity>()
                resultTodoItems.forEach {
                    resultList.add(it)
                    if (it.todoId > todoMaxId)
                        todoMaxId = it.todoId
                }
                resultList.sortBy { it.todoId }
                state = state.copy(
                    todoDataList = resultList,
                    todoMaxId = todoMaxId + 1,
                )
            }
        }

    }
}