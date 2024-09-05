package com.suni.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


/**
 * [RealmObject] 그룹 객체
 * 24.09.02 Create class - Q
 */
class GroupEntity: RealmObject {
    @PrimaryKey
    var groupId: Int = 0                // 그룹 번호
    var order: Int = 0                  // 그룹 순서
    var title: String = ""              // 그룹 이름
    var startColorHex: String = ""      //
    var endColorHex: String = ""        //
    var appColorIndex: Int = 0

//    fun setGroupId(id: Int) {
//        groupId = id
//    }
//    fun getGroupId(): Int = groupId
//    fun setOrder(order: Int) {
//        this.order = order
//    }
//    fun getOrder(): Int = order
//    fun setTitle(title: String) {
//        this.title = title
//    }
//    fun getTitle(): String = title
//    fun setStartColorHex(startColor: String) {
//        startColorHex = startColor
//    }
//    fun getStartColorHex(): String = startColorHex
//    fun setEndColorHex(endColor: String) {
//        endColorHex = endColor
//    }
//    fun getEndColorHex(): String = endColorHex
//    fun setAppColorIndex(index: Int) {
//        appColorIndex = index
//    }
//    fun getAppColorIndex(): Int = appColorIndex
}