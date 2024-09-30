package com.suni.todogroup.ui.modify

import com.suni.data.model.GroupEntity

data class ModifyGroupScreenState(
    val isFinishedModify: Boolean = false,
    val isFinishedDelete: Boolean = false,
    val colorIndex: Int = 0,
    val groupData : GroupEntity = GroupEntity(),
)