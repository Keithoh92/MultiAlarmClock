package com.example.multialarmclock.data

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    val readLastEntered: LiveData<List<BuildNewAlarmModel>> = alarmDao.readLastEntered()
    val readAllData: LiveData<List<BuildNewAlarmModel>> = alarmDao.readAllData()

    suspend fun addAlarm(buildNewAlarmModel: BuildNewAlarmModel){
        alarmDao.addAlarm(buildNewAlarmModel)
    }

    fun deleteAlarm(id: Int) {
        alarmDao.deleteById(id)
    }

    fun updateActiveState(active: Boolean, id: Int) {
        alarmDao.updateActiveState(active, id)
    }

}