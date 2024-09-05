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
        groupId: Int = 0,
        order: Int = 0,
        title: String = "",
        startColorHex: String = "",
        endColorHex: String = "",
        appColorIndex: Int = 0,
    ) {
        realm.writeBlocking {
            copyToRealm(
                GroupEntity().apply {
                    this.groupId = groupId
                    this.order = order
                    this.title = title
                    this.startColorHex = startColorHex
                    this.endColorHex = endColorHex
                    this.appColorIndex = appColorIndex
                }
            )
        }
    }
}