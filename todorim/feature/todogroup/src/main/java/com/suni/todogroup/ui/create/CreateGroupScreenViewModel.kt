package com.suni.todogroup.ui.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.suni.data.model.GroupEntity
import com.suni.domain.usecase.WriteGroupDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] 그룹 생성 뷰모델
 * 24.09.12 Create class - Q
 */
@HiltViewModel
class CreateGroupScreenViewModel @Inject constructor(
    private val writeGroupDataUseCase: WriteGroupDataUseCase,
): ViewModel() {

    var state by mutableStateOf(CreateGroupScreenState())
        private set

    fun onEvent(event: CreateGroupScreenEvents) {
        when(event) {
            is CreateGroupScreenEvents.CreateGroup -> {
                createGroup(event)
            }
            is CreateGroupScreenEvents.SelectGroupColor -> {
                selectGroupColor(event)
            }
        }
    }

    /**
     * 그룹 정보 저장
     * @param event AddGroupItem
     */
    private fun createGroup(event: CreateGroupScreenEvents.CreateGroup) {
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
    }

    /**
     * 그룹 테마 색상 선택
     * @param event
     */
    private fun selectGroupColor(event: CreateGroupScreenEvents.SelectGroupColor) {
        state = state.copy(
            colorIndex = event.colorIndex
        )
    }
}