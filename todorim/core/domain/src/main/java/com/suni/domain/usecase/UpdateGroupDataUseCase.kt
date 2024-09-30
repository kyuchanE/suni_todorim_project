package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

/**
 * [UseCase] 할 일 데이터 수정
 * 24.09.26 Create class - Q
 */
class UpdateGroupDataUseCase @Inject constructor(
    private val realm: Realm
){
    operator fun invoke(
        groupData: GroupEntity
    ) {
        realm.query<GroupEntity>("groupId == $0", groupData.groupId)
            .first()
            .find()
            ?.also { groupEntity ->
                realm.writeBlocking {
                    findLatest(groupEntity)?.title = groupData.title
                    findLatest(groupEntity)?.order = groupData.order
                    findLatest(groupEntity)?.appColorIndex = groupData.appColorIndex
                }
            }
    }
}