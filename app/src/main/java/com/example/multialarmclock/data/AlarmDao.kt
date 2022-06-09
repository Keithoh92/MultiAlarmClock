package com.example.multialarmclock.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(buildNewAlarmDao: BuildNewAlarmDao)

    @Query("SELECT * FROM my_alarms ORDER BY id DESC")
    fun readAllData(): List<BuildNewAlarmDao>

    @Query("SELECT * FROM my_alarms ORDER BY id DESC limit 1")
    fun readLastEntered(): List<BuildNewAlarmDao>

    @Query("DELETE FROM my_alarms WHERE id = :alarmId")
    fun deleteById(alarmId: Int)

    @Query("UPDATE my_alarms SET active = :activeState WHERE id = :alarmId")
    fun updateActiveState(activeState: Boolean, alarmId: Int)
}