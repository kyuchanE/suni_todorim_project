package com.suni.domain.usecase

import com.suni.data.repository.GroupDataRepository
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 삭제
 * 24.09.30 Create class - Q
 */
class DeleteGroupDataUseCase @Inject constructor(
    private val groupDataRepository: GroupDataRepository
) {
    operator fun invoke(groupId: Int) {
        groupDataRepository.deleteGroupData(groupId)
    }
}