package com.example.multialarmclock.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_alarms")
data class BuildNewAlarmDao(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val alarmName:String,
    val daysSelected:String,
    val weekly:Boolean,
    val startTime:String,
    val endTime:String,
    val sound: String,
    val interval:Int,
    val time: String,
    @ColumnInfo(defaultValue = "0") val active: Boolean
)
