package com.example.multialarmclock.data

import android.database.Cursor
import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    val readLastEntered: LiveData<List<BuildNewAlarmModel>> = alarmDao.readLastEntered()
    val readAllData: LiveData<List<BuildNewAlarmModel>> = alarmDao.readAllData()

    suspend fun addAlarm(buildNewAlarmModel: BuildNewAlarmModel){
        alarmDao.addAlarm(buildNewAlarmModel)
    }

}