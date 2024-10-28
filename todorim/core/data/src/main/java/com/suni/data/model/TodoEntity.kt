package com.suni.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * [RealmObject] 할일 객체
 * 24.09.02 Create class - Q
 */
class TodoEntity: RealmObject {
    @PrimaryKey
    var todoId: Int = 0                 // 할일 ID
    var groupId: Int = 0                // 그룹 ID
    var title: String = ""              // 할일 이름
    var isCompleted: Boolean = false    // 완료 여부
    var order: Int = 0                  // 순서
    var isDateNoti: Boolean = false     // 날짜 알림 여부
    var date: String = ""               // 날짜 (yyyy-MM-dd)
    var week: Int = 0                   // 1: 일요일 - 7: 토요일
    var day: Int = 0                    // 1 ~ 31
    var notiTime: String = ""           // 시간 (hh:mm)
    var isLocationNoti: Boolean = false // 위치 알림 여부
    var locationName: String = ""       // 위치 이름
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    var radius: Double = 100.0

//    fun setTodoId(id: Int) {
//        todoId = id
//    }
//    fun getTodoId(): Int = todoId
//    fun setGroupId(id: Int) {
//        groupId = id
//    }
//    fun getGroupId(): Int = groupId
//    fun setTitle(title: String) {
//        this.title = title
//    }
//    fun getTitle(): String = title
//    fun setIsCompleted(isCompleted: Boolean) {
//        this.isCompleted = isCompleted
//    }
//    fun getIsCompleted(): Boolean = isCompleted
//    fun setOrder(order: Int) {
//        this.order = order
//    }
//    fun getOrder(): Int = order
//    fun setIsDateNoti(isDateNoti: Boolean) {
//        this.isDateNoti = isDateNoti
//    }
//    fun getIsDateNoti(): Boolean = isDateNoti
//    fun setDate(date: Date) {
//        this.date = date
//    }
//    fun getDate(): Date? = date
//    fun setWeek(week: Int) {
//        this.week = week
//    }
//    fun getWeek(): Int = week
//    fun setDay(day: Int) {
//        this.day = day
//    }
//    fun getDay(): Int = day
//    fun setIsLocationNoti(isLocationNoti: Boolean) {
//        this.isLocationNoti = isLocationNoti
//    }
//    fun getIsLocationNoti(): Boolean = isLocationNoti
//    fun setLocationName(name: String) {
//        locationName = name
//    }
//    fun getLocationName(): String = locationName
//    fun setLongitude(lon: Double) {
//        longitude = lon
//    }
//    fun getLongitude(): Double = longitude
//    fun setLatitude(lat: Double) {
//        latitude = lat
//    }
//    fun getLatitude(): Double = latitude
//    fun setRadius(radius: Double) {
//        this.radius = radius
//    }
//    fun getRadius(): Double = radius
}