package com.example.multialarmclock.feature.activity.alarmIntervalBuilder

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.multialarmclock.R
import com.example.multialarmclock.data.AlarmRepository
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.feature.activity.alarmIntervalBuilder.utils.CheckedDays
import com.example.multialarmclock.feature.base.liveData.SingleLiveEvent
import com.example.multialarmclock.feature.base.viewModel.AlarmAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class BuildIntervalAlarmViewModel (
    private val alarmRepository: AlarmRepository
    ): AlarmAppViewModel(), KoinComponent {

    interface CreateNewAlarm {
        fun getCheckedDaysTestInterface(): String
    }

    var currentRingtone: Uri? = null
    var chosenRingtoneUri: Uri? = null

    var daysSelectedDisplayString = String()

    val onSuccess = SingleLiveEvent<Long>()

    var buildNewAlarmDao: BuildNewAlarmDao? = null

    fun getAlarmCount(): Int = alarmRepository.getCountOfAlarmsInDB()

    fun createNewAlarm(buildAlarm:BuildNewAlarmDao) {
        viewModelScope.launch(Dispatchers.IO){
            Log.d("BuildNewAlarmVM", "Adding Alarm = $buildAlarm")
            val success = alarmRepository.addAlarm(buildAlarm)
            onSuccess.postCall(success)
        }
    }

    fun createCheckedDaysString(checkedDays: CheckedDays) {
        daysSelectedDisplayString = ""
        val daysSelected = ArrayList<String>()
        if (checkedDays.monday) {
            daysSelected.add(R.string.monday.toString())
        }
        if (checkedDays.tuesday) {
            daysSelected.add(R.string.tuesday.toString())
        }
        if (checkedDays.wednesday) {
            daysSelected.add(R.string.wednesday.toString())
        }
        if (checkedDays.thursday) {
            daysSelected.add(R.string.thursday.toString())
        }
        if (checkedDays.friday) {
            daysSelected.add(R.string.friday.toString())
        }
        if (checkedDays.saturday) {
            daysSelected.add(R.string.saturday.toString())
        }
        if (checkedDays.sunday) {
            daysSelected.add(R.string.sunday.toString())
        }

        var separator = ""
        val sb = StringBuilder()
        for (i in daysSelected.indices){
            sb.append(separator+daysSelected[i])
            separator = ","//MON,TUES
        }
        daysSelectedDisplayString = sb.toString()
    }
}