package com.example.multialarmclock.feature.base.dl.modules

import android.app.Application
import com.example.multialarmclock.data.AlarmDatabase
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.feature.base.dl.DatabaseModule
import com.example.multialarmclock.feature.base.dl.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object AlarmAppModule {

    @JvmStatic
    fun startKoin(application: Application) = startKoin {
        androidContext(application)
        modules(
            listOf(
                ViewModelModule.get(),
                DatabaseModule.get(),
                AlarmRepositoryModule.get()
            )
        )
    }
}