package com.suni.todogroup.ui.detail

import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity

data class GroupDetailScreenState(
    val groupData : GroupEntity = GroupEntity(),
    val todoDataList : MutableList<TodoEntity> = mutableListOf(),
    val todoMaxId: Int = 0,
    val isNeedRefreshHome: Boolean = false,
)