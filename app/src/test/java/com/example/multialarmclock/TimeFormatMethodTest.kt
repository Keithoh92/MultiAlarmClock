package com.example.multialarmclock

import com.example.multialarmclock.feature.activity.alarmIntervalBuilder.BuildIntervalAlarmFragment
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import com.google.common.truth.Truth


@RunWith(JUnit4::class)
class TimeFormatMethodTest {

    val cal = Calendar.getInstance()
    val buildNewAlarm = BuildIntervalAlarmFragment()


    fun formatTimeForDB1(hour:Int, minute:Int): String {

        val h = "%02d".format(if (hour < 12) hour else hour - 12)
        val m = "%02d".format(minute)

        return "$h:$m"
    }

    //When time is less than 12
    @Test
    fun whenTimeIsLessThanTwelve(){
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        val result = buildNewAlarm.formatTimeForDB(hour, minute)
        assertEquals(result, "12:21")
    }

    //When time is less than 12
    @Test
    fun whenTimeIsGreaterThanTwelve(){
        val hour = cal.set(Calendar.HOUR, 8).toString()
        val minute = cal.set(Calendar.MINUTE, 0).toString()
        val h = hour.toInt()
        val m = minute.toInt()
        val result = formatTimeForDB1(h, m)
        val str = "08:00"
        Truth.assertThat(result).isEqualTo(str)
    }



}