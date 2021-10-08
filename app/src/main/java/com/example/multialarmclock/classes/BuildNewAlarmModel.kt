package com.example.multialarmclock.classes

import android.net.Uri

data class BuildNewAlarmModel(

    val alarmName:String,
    val daysSelected:ArrayList<String>,
    val weekly:Boolean,
    val startTime:String,
    val endTime:String,
    val sound: Uri,
    val interval:Int

)
