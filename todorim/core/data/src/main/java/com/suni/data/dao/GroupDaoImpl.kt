package com.suni.data.dao

import com.suni.data.model.GroupEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

/**
 * [GroupDao]
 * 24.09.26 Create class - Q
 */
class GroupDaoImpl @Inject constructor(
    private val realm: Realm
): GroupDao {

    override fun getGroupsAll() =
        realm.query<GroupEntity>().find().asFlow()

    override fun getGroupById(groupId: Int) =
        realm.query<GroupEntity>("groupId = $0", groupId).find().asFlow()

    override fun writeGroup(groupEntity: GroupEntity) {
        realm.writeBlocking {
            copyToRealm(groupEntity)
        }
    }

    override fun deleteGroup(groupId: Int) {
        realm.writeBlocking {
            val resultQuery = this.query<GroupEntity>("groupId == $0", groupId)
            delete(resultQuery)
        }
    }

}