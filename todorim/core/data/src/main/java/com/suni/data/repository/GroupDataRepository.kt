package com.suni.data.repository

import com.suni.data.model.GroupEntity
import kotlinx.coroutines.flow.Flow

interface GroupDataRepository {

    suspend fun getGroupData(): Flow<MutableList<GroupEntity>>

    suspend fun getGroupData(groupId: Int): Flow<GroupEntity>

    fun writeGroupData(groupEntity: GroupEntity)

    suspend fun updateGroupData(updateEntity: GroupEntity)

    fun deleteGroupData(groupId: Int)
}