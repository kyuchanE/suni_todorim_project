package com.suni.todo.navigator

import com.suni.navigator.TodoNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Module] TodoNavigator 모듈
 * 24.09.10 Create - Q
 */
@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    @Singleton
    fun bindMainNavigator(todoNavigatorImpl: TodoNavigatorImpl): TodoNavigator

}