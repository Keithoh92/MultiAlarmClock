package com.example.multialarmclock.data

import org.koin.core.component.KoinComponent

class AlarmRepository(private val alarmDao: AlarmDao) {

//    val readLastEntered: LiveData<List<BuildNewAlarmModel>> = alarmDao.readLastEntered()
//    val readAllData: LiveData<List<BuildNewAlarmModel>> = alarmDao.readAllData()

    fun addAlarm(buildNewAlarmDao: BuildNewAlarmDao): Long {
        return alarmDao.addAlarm(buildNewAlarmDao)
    }

    fun deleteAlarm(id: Int) {
        alarmDao.deleteById(id)
    }

    fun updateActiveState(active: Boolean, id: Int) {
        alarmDao.updateActiveState(active, id)
    }

    fun fetchAllAlarms(): List<BuildNewAlarmDao> = alarmDao.readAllData()

    fun getLastSavedAlarm(): List<BuildNewAlarmDao> = alarmDao.readLastEntered()

}