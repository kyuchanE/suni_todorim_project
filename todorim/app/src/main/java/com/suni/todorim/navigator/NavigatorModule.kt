package com.suni.todorim.navigator

import com.suni.navigator.MainNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Module] MainNavigator 모듈
 * 24.09.10 Create - Q
 */
@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    @Singleton
    fun bindMainNavigator(mainNavigatorImpl: MainNavigatorImpl): MainNavigator
}