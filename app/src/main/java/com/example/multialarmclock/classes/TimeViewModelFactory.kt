package com.example.multialarmclock.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TimeViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimeViewModel::class.java)){
            return TimeViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }

}