package com.example.multialarmclock.classes

data class DisplayModel (
    val hour:Int,
    val minute:Int
){
    val timeText:String
        get(){
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            if(hour < 12 && minute < 10){
                return "0$h:0$m"
            }
            else if(hour < 12 && minute > 9){
                return "0$h:$m"
            }
            else if(hour > 11 && minute < 10){
                return "$h:$0m"
            }
            else{
                return "$h:$m"
            }
    }
}