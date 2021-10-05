package com.example.multialarmclock.classes

import android.widget.RelativeLayout
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField


class BuildAlarmViewModel:BaseObservable(){

    @set:Bindable
    var isChecked: Boolean? = null
}

//private operator fun Boolean.invoke(value: Any): Any {
//    this.invoke(true)
//}


//@BindingAdapter("android:layout_height")
//fun setLayoutHeight(view: RelativeLayout, height: Float?) {
//    view.layoutParams = view.layoutParams.apply {
//        if (height != null) {
//            this.height = height.toInt()
//        }
//    }
//}