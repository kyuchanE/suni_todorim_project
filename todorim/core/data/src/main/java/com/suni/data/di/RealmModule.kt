package com.suni.data.di

import com.suni.data.model.GroupEntity
import com.suni.data.model.TodoEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

/**
 * [Module] Realm 모듈
 * 24.09.04 Create object - Q
 */
@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun providerRealm(
        configuration: RealmConfiguration
    ): Realm = Realm.open(configuration)

    @Provides
    @Singleton
    fun providerRealmConfiguration(): RealmConfiguration =
        RealmConfiguration.Builder(schema = setOf(GroupEntity::class, TodoEntity::class))
            .name("TDR_REALM")
            .schemaVersion(0) // migration 을 위해 추가
            .deleteRealmIfMigrationNeeded()
            .build()

}