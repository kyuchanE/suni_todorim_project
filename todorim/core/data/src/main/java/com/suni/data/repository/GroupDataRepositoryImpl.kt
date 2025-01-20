package com.suni.data.repository

import com.suni.data.dao.GroupDao
import com.suni.data.model.GroupEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [GroupDataRepository]
 * 24.09.26 Create class - Q
 */
class GroupDataRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val realm: Realm,
) : GroupDataRepository {

    /**
     * 그룹 데이터 전체 조회
     */
    override suspend fun getGroupData(): Flow<MutableList<GroupEntity>> =
        groupDao.getGroupsAll().map { resultsChange ->
            resultsChange.list.toMutableList()
        }

    /**
     * 그룹 데이터 조회
     * @param groupId
     */
    override suspend fun getGroupData(groupId: Int): Flow<GroupEntity> =
        groupDao.getGroupById(groupId).map { resultsChange ->
            resultsChange.list.first()
        }

    /**
     * 그룹 데이터 추가
     * @param groupEntity 그룹 엔티티
     */
    override fun writeGroupData(groupEntity: GroupEntity) {
        groupDao.writeGroup(groupEntity)
    }

    /**
     * 그룹 데이터 업데이트
     * @param updateEntity 업데이트 할 그룹 엔티티
     */
    override suspend fun updateGroupData(updateEntity: GroupEntity) {
        groupDao.getGroupById(updateEntity.groupId).collectLatest { resultsChange ->
            resultsChange.list.last().let { groupEntity ->
                realm.writeBlocking {
                    findLatest(groupEntity)?.title = updateEntity.title
                    findLatest(groupEntity)?.order = updateEntity.order
                    findLatest(groupEntity)?.appColorIndex = updateEntity.appColorIndex
                }
            }
        }
    }

    /**
     * 그룹 데이터 삭제
     */
    override fun deleteGroupData(groupId: Int) {
        groupDao.deleteGroup(groupId)
    }
}