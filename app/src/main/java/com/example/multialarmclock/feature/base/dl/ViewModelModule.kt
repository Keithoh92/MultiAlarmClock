package com.example.multialarmclock.feature.base.dl

import com.example.multialarmclock.feature.activity.alarmIntervalBuilder.BuildIntervalAlarmViewModel
import com.example.multialarmclock.feature.base.dl.modules.ModuleInterface
import com.example.multialarmclock.feature.activity.homeScreen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ViewModelModule: ModuleInterface {
    override fun get() = module {

        viewModel { HomeScreenViewModel(get()) }

        viewModel { BuildIntervalAlarmViewModel(get()) }

    }
}