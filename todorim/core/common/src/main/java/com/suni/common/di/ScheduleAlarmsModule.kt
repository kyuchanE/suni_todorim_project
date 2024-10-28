package com.suni.common.di

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Module] ScheduleAlarms 모듈
 * 24.10.21 Create object - Q
 */
@Module
@InstallIn(SingletonComponent::class)
object ScheduleAlarmsModule {

        @Provides
        @Singleton
        fun providerScheduleAlarmManager(@ApplicationContext context: Context): AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

}