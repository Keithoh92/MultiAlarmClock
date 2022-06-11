package com.example.multialarmclock

import android.app.Application
import com.example.multialarmclock.feature.base.dl.modules.AlarmAppModule.startKoin
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.multialarmclock.feature.base.dl.modules.AlarmAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MultiAlarmApp : Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        instance = this
        AlarmAppModule.startKoin(this)
    }

    companion object {
        var instance: MultiAlarmApp? = null
    }

}