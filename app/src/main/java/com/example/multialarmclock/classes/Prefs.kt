package com.example.multialarmclock.classes

import android.content.Context
import android.content.SharedPreferences
import com.example.multialarmclock.prefs

class Prefs (context: Context){

    private val savedAlarms = "savedAlarms"

//    private val ALARM_ON_ID_2 = "alarmOnOff2"
//    private val ALARM_ON_ID_3 = "alarmOnOff3"
//    private val ALARM_ON_ID_4 = "alarmOnOff4"
//    private val ALARM_ON_ID_5 = "alarmOnOff5"

    private val alarmPref1: SharedPreferences = context.getSharedPreferences(savedAlarms, Context.MODE_PRIVATE)
//    private val alarmPref2: SharedPreferences = context.getSharedPreferences(ALARM_ON_ID_2, Context.MODE_PRIVATE)
//    private val alarmPref3: SharedPreferences = context.getSharedPreferences(ALARM_ON_ID_3, Context.MODE_PRIVATE)
//    private val alarmPref4: SharedPreferences = context.getSharedPreferences(ALARM_ON_ID_4, Context.MODE_PRIVATE)
//    private val alarmPref5: SharedPreferences = context.getSharedPreferences(ALARM_ON_ID_5, Context.MODE_PRIVATE)

//    var alarmIdPref1: Boolean
//        get() = alarmPref1.getBoolean(ALARM_ON_ID_1, false)
//        set(value) = alarmPref1.edit().putBoolean(ALARM_ON_ID_1, value).apply()
//
//    var alarmIdPref2: Boolean
//        get() = alarmPref2.getBoolean(ALARM_ON_ID_2, false)
//        set(value) = alarmPref2.edit().putBoolean(ALARM_ON_ID_2, value).apply()
//
//    var alarmIdPref3: Boolean
//        get() = alarmPref3.getBoolean(ALARM_ON_ID_3, false)
//        set(value) = alarmPref3.edit().putBoolean(ALARM_ON_ID_3, value).apply()
//
//    var alarmIdPref4: Boolean
//        get() = alarmPref4.getBoolean(ALARM_ON_ID_4, false)
//        set(value) = alarmPref4.edit().putBoolean(ALARM_ON_ID_4, value).apply()
//
//    var alarmIdPref5: Boolean
//        get() = alarmPref5.getBoolean(ALARM_ON_ID_5, false)
//        set(value) = alarmPref5.edit().putBoolean(ALARM_ON_ID_5, value).apply()

//    fun getAll(): HashMap<String, Boolean> {
//
//        return prefMap
////        var allPrefs = listOf(
////            "alarmOnOff1",
////            "alarmOnOff2",
////            "alarmOnOff3",
////            "alarmOnOff4",
////            "alarmOnOff5")
////        return allPrefs
//    }



}