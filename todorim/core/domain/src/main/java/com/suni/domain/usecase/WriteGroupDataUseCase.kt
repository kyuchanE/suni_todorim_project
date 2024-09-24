package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import io.realm.kotlin.Realm
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 저장
 * 24.09.04 Create class - Q
 */
class WriteGroupDataUseCase @Inject constructor(
    private val realm: Realm
) {
    operator fun invoke(
        groupData: GroupEntity
    ) {
        realm.writeBlocking {
            copyToRealm(groupData)
        }
    }
}