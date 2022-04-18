package com.example.multialarmclock.feature.alarmIntervalBuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.multialarmclock.R

class BuildIntervalAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build_interval_alarm)

        supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, AlarmIntervalBuilder.newInstance())
            .commit()
    }
}