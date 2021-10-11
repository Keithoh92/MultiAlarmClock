package com.example.multialarmclock.classes

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

class TimeViewModel: ViewModel() {

    val theHour = MutableLiveData<Int>()//09:00
    val theMinute = MutableLiveData<Int>()


    fun setTime(mHour: Int, mMinute:Int){
        theHour.value = mHour
        theMinute.value = mMinute
    }

    fun getTime():String{
        var newTime:String = ""
        var hour = theHour.value?.toInt()
        var minute = theMinute.value?.toInt()
        val h = "%02d".format(if (hour!! < 12) hour +1 else hour - 12+1)
        val m = "%02d".format(minute)

        return "$h:$m"
    }


}