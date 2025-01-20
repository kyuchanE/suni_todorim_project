package com.suni.data.dao

import com.suni.data.model.GroupEntity
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

interface GroupDao {
    // 모든 그룹 데이터 조회
    fun getGroupsAll(): Flow<ResultsChange<GroupEntity>>
    // 특정 그룹 데이터 조회
    fun getGroupById(groupId: Int): Flow<ResultsChange<GroupEntity>>
    // 그룹 데이터 추가
    fun writeGroup(groupEntity: GroupEntity)
    // 그룹 데이터 삭제
    fun deleteGroup(groupId: Int)
}