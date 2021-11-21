package com.example.multialarmclock.classes

data class DisplayModel (
    val hour:Int,
    val minute:Int
){
    val timeText:String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
    }
//    val ampmText: String
//    get(){
//        return if (onOff)
//    }
}