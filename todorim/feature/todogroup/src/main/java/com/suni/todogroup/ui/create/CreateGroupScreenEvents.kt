package com.suni.todogroup.ui.create

sealed class CreateGroupScreenEvents {
    data class CreateGroup(
        val groupId: Int = 0,
        val order: Int = 0,
        val title: String = "",
        val startColorHex: String = "",
        val endColorHex: String = "",
        val appColorIndex: Int = 0,
    ): CreateGroupScreenEvents()

    data class SelectGroupColor(
        val colorIndex: Int = 0,
    ): CreateGroupScreenEvents()
}