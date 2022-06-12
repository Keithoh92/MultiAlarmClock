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

    private val _readAllData = SingleLiveEvent<List<BuildNewAlarmDao>>()
    val readAllData = _readAllData as LiveData<List<BuildNewAlarmDao>>

    private val _readLastEntered = SingleLiveEvent<List<BuildNewAlarmDao>>()
    val readLastEntered = _readLastEntered as LiveData<List<BuildNewAlarmDao>>

    val onSuccess = SingleLiveEvent<Long>()

    fun getAllAlarms() = viewModelScope.launch {
        _readAllData.postCall(alarmRepository.fetchAllAlarms())
    }

    fun getLastSavedAlarm() = viewModelScope.launch {
        _readLastEntered.postCall(alarmRepository.getLastSavedAlarm())
    }

    fun addAlarm(buildAlarm:BuildNewAlarmDao) {
        viewModelScope.launch(Dispatchers.IO){
            Log.d("BuildNewAlarmVM", "Adding Alarm = $buildAlarm")
            val success = alarmRepository.addAlarm(buildAlarm)
            onSuccess.postCall(success)
        }
    }

    fun deleteAlarm(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.deleteAlarm(id)
            Log.d("AlarmViewModel", "Deleting alarm")
        }
    }

    fun updateActiveState(activeState: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.updateActiveState(activeState, id)
            Log.d("AlarmViewModel", "Updating State of alarm: $id to $activeState")
        }
    }

}