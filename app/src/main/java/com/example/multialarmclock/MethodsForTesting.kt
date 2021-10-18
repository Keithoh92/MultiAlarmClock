package com.example.multialarmclock

import android.util.Log
import android.widget.CheckBox

object MethodsForTesting {

    fun formatTimeForDB1(hour:Int, minute:Int): String {

        val h = "%02d".format(if (hour < 12) hour else hour - 12)
        val m = "%02d".format(minute)

        return "$h:$m"
    }

//    private fun getCheckedDays(): String {
//        var daysSelected:ArrayList<String> = arrayListOf()
//        var cbDay1:CheckBox =
//        cbDay1.isChecked = true
//
//        if(cbDay1.isChecked){
//            Log.d("DaysCheccked: ", cbDay1.text.toString())
//            daysSelected.add("Mon")
//        }
//        if(cbDay2.isChecked){
//            Log.d("DaysCheccked: ", cbDay2.text.toString())
//            daysSelected.add("Tue")
//        }
//        if(cbDay3.isChecked){
//            Log.d("DaysCheccked: ", cbDay3.text.toString())
//            daysSelected.add("Wed")
//        }
//        if(cbDay4.isChecked){
//            Log.d("DaysCheccked: ", cbDay4.text.toString())
//            daysSelected.add("Thu")
//        }
//        if(cbDay5.isChecked){
//            Log.d("DaysCheccked: ", cbDay5.text.toString())
//            daysSelected.add("Fri")
//        }
//        if(cbDay6.isChecked){
//            Log.d("DaysCheccked: ", cbDay6.text.toString())
//            daysSelected.add("Sat")
//        }
//        if(cbDay7.isChecked){
//            Log.d("DaysCheccked: ", cbDay7.text.toString())
//            daysSelected.add("Sun")
//        }
//        var separator = ""
//        var sb = StringBuilder()
//        for (i in daysSelected.indices){
//            sb.append(separator+daysSelected[i])
//            separator = ","//MON,TUES
//        }
//        return sb.toString()
//    }
}