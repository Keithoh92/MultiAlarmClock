package com.example.multialarmclock.data

class AlarmRepository(private val alarmDao: AlarmDao) {

//    val readLastEntered: LiveData<List<BuildNewAlarmModel>> = alarmDao.readLastEntered()
//    val readAllData: LiveData<List<BuildNewAlarmModel>> = alarmDao.readAllData()

    suspend fun addAlarm(buildNewAlarmDao: BuildNewAlarmDao){
        alarmDao.addAlarm(buildNewAlarmDao)
    }

    fun deleteAlarm(id: Int) {
        alarmDao.deleteById(id)
    }

    fun updateActiveState(active: Boolean, id: Int) {
        alarmDao.updateActiveState(active, id)
    }

    fun fetchAllAlarms() = alarmDao.readAllData()

    fun getLastSavedAlarm() = alarmDao.readLastEntered()

}