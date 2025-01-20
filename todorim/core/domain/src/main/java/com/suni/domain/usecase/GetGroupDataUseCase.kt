package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import com.suni.data.repository.GroupDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 조회
 * 24.09.04 Create class - Q
 */
class GetGroupDataUseCase @Inject constructor(
    private val groupDataRepository: GroupDataRepository,
) {

    suspend fun getGroupsAll(): Flow<MutableList<GroupEntity>> =
        groupDataRepository.getGroupData()

    suspend fun getGroupById(groupId: Int): Flow<GroupEntity> =
        groupDataRepository.getGroupData(groupId)

}