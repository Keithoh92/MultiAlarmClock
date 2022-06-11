package com.example.multialarmclock.feature.base.dl

import android.app.Application
import androidx.room.Room
import com.example.multialarmclock.data.AlarmDao
import com.example.multialarmclock.data.AlarmDatabase
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.feature.base.dl.modules.AlarmAppModule
import com.example.multialarmclock.feature.base.dl.modules.ModuleInterface
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DatabaseModule: ModuleInterface {
    override fun get() = module {
        fun provideDataBase(application: Application): AlarmDatabase {
            return Room.databaseBuilder(application, AlarmDatabase::class.java, "ALARMDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }

        fun provideDao(dataBase: AlarmDatabase): AlarmDao {
            return dataBase.alarmDao
        }

        single { provideDataBase(androidApplication()) }
        single { provideDao(get()) }

        single { AlarmRepository(get()) }

//        single { AlarmDao }
//
//        single {
//            AlarmRepository(get())
//        }

//        factoryOf { AlarmRepository(alarmDao = provideDao()) }

    }
}