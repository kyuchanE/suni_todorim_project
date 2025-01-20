package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import com.suni.data.repository.GroupDataRepository
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 수정
 * 24.09.26 Create class - Q
 */
class UpdateGroupDataUseCase @Inject constructor(
    private val groupDataRepository: GroupDataRepository,
){
    suspend operator fun invoke(
        groupData: GroupEntity,
    ) {
        groupDataRepository.updateGroupData(groupData)
    }
}