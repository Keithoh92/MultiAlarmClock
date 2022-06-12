package com.example.multialarmclock.data

import org.koin.core.component.KoinComponent

class AlarmRepository(private val alarmDao: AlarmDao) {

    fun fetchAllAlarms(): List<BuildNewAlarmDao> = alarmDao.readAllData()

    fun getLastSavedAlarm(): List<BuildNewAlarmDao> = alarmDao.readLastEntered()

    fun getCountOfAlarmsInDB(): Int = alarmDao.getCountOfAlarms()

    fun addAlarm(buildNewAlarmDao: BuildNewAlarmDao): Long {
        return alarmDao.addAlarm(buildNewAlarmDao)
    }

    fun deleteAlarm(id: Int) {
        alarmDao.deleteById(id)
    }

    fun updateActiveState(active: Boolean, id: Int) {
        alarmDao.updateActiveState(active, id)
    }

}