package com.example.multialarmclock.classes

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeViewModel: ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Int>()
    private val mutableSelectedItem2 = MutableLiveData<Int>()
    val selectedHour: LiveData<Int> get() = mutableSelectedItem
    val selectedMinute: LiveData<Int> get() = mutableSelectedItem2

    fun selectItems(p1: Int, p2: Int){
        mutableSelectedItem.value = p1
        mutableSelectedItem2.value = p2
    }

}