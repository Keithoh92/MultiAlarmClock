package com.example.multialarmclock

import com.example.multialarmclock.feature.base.dl.DatabaseModule
import com.example.multialarmclock.feature.base.dl.ViewModelModule
import com.example.multialarmclock.feature.base.dl.modules.AlarmRepositoryModule
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

class ModuleTest: KoinTest {

    // Declare Mock with Mockito
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    // verify the Koin configuration
    @Test
    fun checkAllModules() = checkModules {
        modules(
            ViewModelModule.get(),
            DatabaseModule.get(),
            AlarmRepositoryModule.get()
        )
    }
}