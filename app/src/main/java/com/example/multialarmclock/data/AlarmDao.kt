package com.example.multialarmclock.data

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(buildNewAlarmModel: BuildNewAlarmModel)

    @Query("SELECT * FROM my_alarms ORDER BY id DESC")
    fun readAllData(): LiveData<List<BuildNewAlarmModel>>

    @Query("SELECT alarmName FROM my_alarms ORDER BY id DESC limit 1")
    fun readLastEntered(): LiveData<String>
}