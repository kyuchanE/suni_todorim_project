package com.suni.todorim

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * [Application] class for ToDoRim
 * 240902 Start Project - Q
 */
@HiltAndroidApp
class TdrApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}