package com.example.multialarmclock.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "my_alarms")
data class BuildNewAlarmModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val alarmName:String,
    val daysSelected:ArrayList<String>,
    val weekly:Boolean,
    val startTime:CharSequence,
    val endTime:CharSequence,
    val sound: Uri,
    val interval:Int,
    val time: Time

)