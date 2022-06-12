package com.example.multialarmclock.feature.activity.alarmIntervalBuilder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.feature.base.liveData.SingleLiveEvent
import com.example.multialarmclock.feature.base.viewModel.AlarmAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class BuildIntervalAlarmViewModel (
    private val alarmRepository: AlarmRepository
    ): AlarmAppViewModel(), KoinComponent {

    val onSuccess = SingleLiveEvent<Long>()

    fun getAlarmCount(): Int = alarmRepository.getCountOfAlarmsInDB()

    fun addAlarm(buildAlarm:BuildNewAlarmDao) {
        viewModelScope.launch(Dispatchers.IO){
            Log.d("BuildNewAlarmVM", "Adding Alarm = $buildAlarm")
            val success = alarmRepository.addAlarm(buildAlarm)
            onSuccess.postCall(success)
        }
    }
}