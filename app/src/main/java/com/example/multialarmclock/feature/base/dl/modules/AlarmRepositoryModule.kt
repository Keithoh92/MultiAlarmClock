package com.example.multialarmclock.feature.base.dl.modules

import com.example.multialarmclock.data.AlarmDao
import com.example.multialarmclock.data.AlarmRepository
import org.koin.dsl.module

object AlarmRepositoryModule: ModuleInterface {
    override fun get() = module {
        fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository {
            return AlarmRepository(alarmDao)
        }

        single { provideAlarmRepository(get()) }
    }

}