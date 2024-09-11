package com.suni.todogroup.navigator

import com.suni.navigator.GroupNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Module] GroupNavigator 모듈
 * 24.09.11 Create - Q
 */
@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    @Singleton
    fun bindGroupNavigator(groupNavigatorImpl: GroupNavigatorImpl): GroupNavigator
}