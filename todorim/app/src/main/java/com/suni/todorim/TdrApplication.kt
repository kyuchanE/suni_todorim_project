package com.suni.todorim

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm

/**
 * [Application] class for ToDoRim
 * 24.09.02 Start Project - Q
 */
@HiltAndroidApp
class TdrApplication: Application() {
    override fun onCreate() {
        super.onCreate()


    }
}