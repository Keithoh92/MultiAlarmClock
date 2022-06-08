package com.example.multialarmclock.feature.homeScreen

import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.data.BuildNewAlarmModel
import com.example.multialarmclock.feature.base.liveData.SingleLiveEvent
import com.example.multialarmclock.feature.base.viewModel.AlarmAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel(private val alarmRepository: AlarmRepository): AlarmAppViewModel() {

    private val _readAllData = SingleLiveEvent<List<BuildNewAlarmModel>>()
    val readAllData = _readAllData as LiveData<List<BuildNewAlarmModel>>

    private val _readLastEntered = SingleLiveEvent<List<BuildNewAlarmModel>>()
    val readLastEntered = _readLastEntered as LiveData<List<BuildNewAlarmModel>>

    fun getAllAlarms() = viewModelScope.launch {
        _readAllData.postCall(alarmRepository.fetchAllAlarms())
    }

    fun getLastSavedAlarm() = viewModelScope.launch {
        _readLastEntered.postCall(alarmRepository.getLastSavedAlarm())
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