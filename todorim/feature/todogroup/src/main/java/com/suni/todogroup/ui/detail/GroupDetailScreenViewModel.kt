package com.suni.todogroup.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suni.data.model.GroupEntity
import com.suni.domain.usecase.GetGroupDataUseCase
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
): ViewModel() {

    var state by mutableStateOf(GroupDetailScreenState())
        private set

    fun onEvent(event: GroupDetailScreenEvents) {
        when(event) {
            is GroupDetailScreenEvents.LoadGroupData -> {
                fetchGroupItem(event)
            }
        }
    }

    private fun fetchGroupItem(event: GroupDetailScreenEvents.LoadGroupData) {
        val result = getGroupDataUseCase.getGroupById(event.groupId)
        if (result.isNotEmpty()) {
            state = state.copy(
                groupData = result.first()
            )
        }
    }
}