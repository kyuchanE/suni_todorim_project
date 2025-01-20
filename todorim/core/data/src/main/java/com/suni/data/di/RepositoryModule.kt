package com.suni.data.di

import android.app.AlarmManager
import android.content.Context
import com.suni.data.dao.GroupDao
import com.suni.data.dao.TodoDao
import com.suni.data.repository.GroupDataRepository
import com.suni.data.repository.GroupDataRepositoryImpl
import com.suni.data.repository.TodoDataRepository
import com.suni.data.repository.TodoDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import javax.inject.Singleton

/**
 * [Module] Repository 모듈
 * 24.09.04 Create object - Q
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providerGroupRepository(
        realm: Realm,
        groupDao: GroupDao,
    ): GroupDataRepository = GroupDataRepositoryImpl(
        groupDao,
        realm,
    )

    @Provides
    @Singleton
    fun providerTodoRepository(
        realm: Realm,
        todoDao: TodoDao,
        scheduleAlarmManager: AlarmManager,
        @ApplicationContext context: Context,
    ): TodoDataRepository = TodoDataRepositoryImpl(
        todoDao,
        realm,
        scheduleAlarmManager,
        context,
    )
}