package com.suni.todogroup.ui.detail

sealed class GroupDetailScreenEvents {
    data class LoadGroupData(val groupId: Int) : GroupDetailScreenEvents()
}