package com.suni.domain.usecase

import com.suni.data.model.GroupEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import javax.inject.Inject

/**
 * [UseCase] 그룹 데이터 조회
 * 24.09.04 Create class - Q
 */
class GetGroupDataUseCase @Inject constructor(
    private val realm: Realm,
) {

    fun getGroupsAll(): RealmResults<GroupEntity> =
        realm.query<GroupEntity>().find()

    fun getGroupById(id: Int): RealmResults<GroupEntity> =
        realm.query<GroupEntity>("groupId = $0", id).find()
}