package com.suni.data.di

import com.suni.data.dao.GroupDao
import com.suni.data.dao.GroupDaoImpl
import com.suni.data.dao.TodoDao
import com.suni.data.dao.TodoDaoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import javax.inject.Singleton

/**
 * [Module] Dao 모듈
 * 24.09.04 Create object - Q
 */
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providerGroupDao(
        realm: Realm,
    ): GroupDao = GroupDaoImpl(realm)

    @Provides
    @Singleton
    fun providerTodoDao(
        realm: Realm,
    ): TodoDao = TodoDaoImpl(realm)
}