package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 삭제
 * 24.09.30 Create class - Q
 */
class DeleteGroupDataUseCase @Inject constructor(
    private val realm: Realm
) {
    operator fun invoke(groupId: Int) {
        realm.writeBlocking {
            val resultQuery = this.query<GroupEntity>("groupId == $0", groupId)
            delete(resultQuery)
        }
    }
}