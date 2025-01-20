package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import com.suni.data.repository.GroupDataRepository
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 저장
 * 24.09.04 Create class - Q
 */
class WriteGroupDataUseCase @Inject constructor(
    private val groupDataRepository: GroupDataRepository,
) {
    operator fun invoke(
        groupData: GroupEntity
    ) {
        groupDataRepository.writeGroupData(groupData)
    }
}