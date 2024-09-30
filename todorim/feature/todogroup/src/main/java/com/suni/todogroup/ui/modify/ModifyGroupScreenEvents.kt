package com.suni.todogroup.ui.modify

sealed class ModifyGroupScreenEvents {
    data class ModifyGroup(
        val groupId: Int = 0,
        val order: Int = 0,
        val title: String = "",
        val startColorHex: String = "",
        val endColorHex: String = "",
        val appColorIndex: Int = 0,
    ): ModifyGroupScreenEvents()

    data class DeleteGroup(
        val groupId: Int = 0,
    ): ModifyGroupScreenEvents()

    data class SelectGroupColor(
        val colorIndex: Int = 0,
    ): ModifyGroupScreenEvents()

    data class LoadGroupData(
        val groupId: Int = 0,
    ): ModifyGroupScreenEvents()
}