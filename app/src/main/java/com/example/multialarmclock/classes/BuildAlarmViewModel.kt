package com.example.multialarmclock.classes

import android.net.Uri
import android.widget.RelativeLayout
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel


class BuildAlarmViewModel: ViewModel() {

    val alarmName:String? = TODO()
    val daysSelected:ArrayList<String> = TODO()
    val weekly:Boolean? = false
    val startTime:String? = TODO()
    val endTime:String? = TODO()
    val sound: Uri? = TODO()
    val interval:Int? = null
}