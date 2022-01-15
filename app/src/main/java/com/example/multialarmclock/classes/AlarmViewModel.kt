package com.example.multialarmclock.classes

import android.app.Application
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.multialarmclock.data.AlarmDatabase
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.data.BuildNewAlarmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@InternalCoroutinesApi
class AlarmViewModel(application: Application): AndroidViewModel(application) {

    val readAllData:LiveData<List<BuildNewAlarmModel>>

    val readLastEntered: LiveData<List<BuildNewAlarmModel>>

    private val repository:AlarmRepository
    init{
        val alarmDao = AlarmDatabase.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        readAllData = repository.readAllData
        readLastEntered = repository.readLastEntered
    }

    fun addAlarm(buildAlarm:BuildNewAlarmModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.addAlarm(buildAlarm)
        }
    }

    fun deleteAlarm(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(id)
            Log.d("AlarmViewModel", "Deleting alarm")
        }
    }

    fun updateActiveState(activeState: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateActiveState(activeState, id)
            Log.d("AlarmViewModel", "Updating State of alarm: $id to $activeState")
        }
    }

}