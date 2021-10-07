package com.example.multialarmclock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Intent
import android.database.Cursor
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.multialarmclock.databinding.ActivityBuildNewAlarmBinding

import com.ramotion.fluidslider.FluidSlider
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BuildNewAlarm : AppCompatActivity() {

    private lateinit var binding: ActivityBuildNewAlarmBinding
    @RequiresApi(Build.VERSION_CODES.P)
    internal lateinit var slider:FluidSlider

    val cal = Calendar.getInstance()

    internal lateinit var startTimeTv:TextView
    internal lateinit var endTimeTv:TextView

    internal lateinit var daysSelected:ArrayList<String>
    internal lateinit var cbDay1:CheckBox
    internal lateinit var cbDay2:CheckBox
    internal lateinit var cbDay3:CheckBox
    internal lateinit var cbDay4:CheckBox
    internal lateinit var cbDay5:CheckBox
    internal lateinit var cbDay6:CheckBox
    internal lateinit var cbDay7:CheckBox

    internal var min: Double = 00.00
    internal var max: Double = 00.00

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("LongLogTag", "RestrictedApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildNewAlarmBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_build_new_alarm)

        //VIEWS INITIALISATION
        setSupportActionBar(binding.mytoolbarBuildAlarm)
//        slider = findViewById(R.id.fluid_slider)
        binding.shapeCorner
        binding.radioGroup
        binding.toggleOn
        binding.toggleOff
        binding.radioGroupTv

        ///////////////////////// ALARM DAY CHOICES /////////////////////////////////////
        cbDay1 = findViewById(R.id.cb_day1)
        cbDay2 = findViewById(R.id.cb_day2)
        cbDay3 = findViewById(R.id.cb_day3)
        cbDay4 = findViewById(R.id.cb_day4)
        cbDay5 = findViewById(R.id.cb_day5)
        cbDay6 = findViewById(R.id.cb_day6)
        cbDay7 = findViewById(R.id.cb_day7)

        daysSelected = arrayListOf()

        //////////////////////////////////      ALARM TIME LOGIC  ///////////////////////////////////////
        val startButton = findViewById<Button>(R.id.open_time_picker_start)
        val endButton = findViewById<Button>(R.id.open_time_picker_end)

        startTimeTv = findViewById(R.id.start_time_tv)
        endTimeTv = findViewById(R.id.end_time_tv)

        startButton.setOnClickListener{
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                startTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)

//                val timeText:String
//                get(){
//                    val h
//                }
            
            }
            TimePickerDialog(this, timeSetlistener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        endButton.setOnClickListener{
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                endTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetlistener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        //////////////////////////////// END OF ALARM TIME LOGIC  //////////////////////////////////////////////////

        /////////////////////////////// INTERVAL TIME PICKER //////////////////////////////////////////////
        val intervalPicker = findViewById<NumberPicker>(R.id.interval_picker)
        intervalPicker.minValue = 0
        intervalPicker.maxValue = 60
        intervalPicker.setFormatter {
            String.format("%02d", it)
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        val saveButton = findViewById(R.id.save_button) as Button
        saveButton.setOnClickListener{
            getCheckedDays()
        }
        var alarmSo = findViewById(R.id.alarm_sound) as TextView

        val ringtone = RingtoneManager(this)
        val currentRingtone: Uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)
//        alarmSo.text = RingtoneManager.TYPE_RINGTONE.toString()
//        currentRingtone.
//        ringtone.cursor.
//        currentRingtone.
        val cursor = ringtone.cursor
        val rt: Ringtone = RingtoneManager.getRingtone(this, currentRingtone)
        val rt1 = rt.getTitle(this)
        Log.d("RT", rt1.toString())

        val rt_tv = findViewById<TextView>(R.id.ringtone_tv)
        rt_tv.text = "Current:\n"+rt1.toString()
//        while (cursor.moveToNext()){
//            var title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
//            Log.d("RT", title)
//        }


       val playButton = findViewById<ImageButton>(R.id.play_button)
       val stopButton = findViewById<ImageButton>(R.id.stop_button)
       val chooseRingtone = findViewById<ImageButton>(R.id.choose_ringtone)

        playButton.setOnClickListener{
            rt.play()
            playButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
        }
        stopButton.setOnClickListener{
            rt.stop()
            stopButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }
        chooseRingtone.setOnClickListener{
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
        }


    }////////////////////// END OF ON CREATE ///////////////////////////

//    private fun getTime(hour:Int, min:Int) {
//        get(){
//            val h = "%02d".format(if(hour < 12) hour else hour - 12)
//            val m = "%02d".format(min)
//
//            return "$h:$m"
//        }
//    }


    private fun getCheckedDays() {
        if(cbDay1.isChecked){
            Log.d("DaysCheccked: ", cbDay1.text.toString())
        }
        if(cbDay2.isChecked){
            Log.d("DaysCheccked: ", cbDay2.text.toString())
        }
        if(cbDay3.isChecked){
            Log.d("DaysCheccked: ", cbDay3.text.toString())
        }
        if(cbDay4.isChecked){
            Log.d("DaysCheccked: ", cbDay4.text.toString())
        }
        if(cbDay5.isChecked){
            Log.d("DaysCheccked: ", cbDay5.text.toString())
        }
        if(cbDay6.isChecked){
            Log.d("DaysCheccked: ", cbDay6.text.toString())
        }
        if(cbDay7.isChecked){
            Log.d("DaysCheccked: ", cbDay7.text.toString())
        }
    }
}
