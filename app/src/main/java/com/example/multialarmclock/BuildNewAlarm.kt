package com.example.multialarmclock

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.multialarmclock.classes.AlarmViewModel
import com.example.multialarmclock.classes.DisplayModel
import com.example.multialarmclock.classes.TimeViewModel
import com.example.multialarmclock.classes.TimeViewModelFactory
import com.example.multialarmclock.data.BuildNewAlarmModel
import com.example.multialarmclock.databinding.ActivityBuildNewAlarmBinding

import com.ramotion.fluidslider.FluidSlider
import kotlinx.coroutines.InternalCoroutinesApi
import nl.joery.timerangepicker.TimeRangePicker
import nl.joery.timerangepicker.TimeRangePicker.Thumb.START
import java.lang.reflect.Array.get
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.hours

class BuildNewAlarm : AppCompatActivity() {

    private lateinit var binding: ActivityBuildNewAlarmBinding
    @RequiresApi(Build.VERSION_CODES.P)
    internal lateinit var slider:FluidSlider

    @InternalCoroutinesApi
    private lateinit var mAlarmViewModel:AlarmViewModel
    internal lateinit var myTimeDisplayViewmodel:TimeViewModel

    val cal = Calendar.getInstance()

    internal lateinit var startTimeTv:TextView
//    internal lateinit var endTimeTv:TextView
    internal lateinit var alarmName:EditText

    internal lateinit var toggleOn:RadioButton
    internal lateinit var toggleOff:RadioButton

    internal lateinit var daysSelected:ArrayList<String>
    internal lateinit var cbDay1:CheckBox
    internal lateinit var cbDay2:CheckBox
    internal lateinit var cbDay3:CheckBox
    internal lateinit var cbDay4:CheckBox
    internal lateinit var cbDay5:CheckBox
    internal lateinit var cbDay6:CheckBox
    internal lateinit var cbDay7:CheckBox

    internal lateinit var timeStart:String
    internal lateinit var timeEnd:String
    internal lateinit var picker:TimeRangePicker
//    internal lateinit var chooseTime:ImageButton
    internal lateinit var timeRangeCV:CardView
    internal lateinit var starttimeTV:TextView
    internal lateinit var endTimeTV:TextView

    internal lateinit var rt_tv:TextView
    internal lateinit var ringtoneDefault:Ringtone
    internal var chosenRingtone:Ringtone? = null
    internal lateinit var currentRingtone:Uri
    var chosenRTUri:Uri?=null
    internal lateinit var intervalPicker:NumberPicker

    var dbInsertionSuccessful:Long = 0

    internal var min: Double = 00.00
    internal var max: Double = 00.00

    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("LongLogTag", "RestrictedApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_build_new_alarm)
        val factory = TimeViewModelFactory()
        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        myTimeDisplayViewmodel = ViewModelProvider(this).get(TimeViewModel::class.java)
        binding.timeViewModel = myTimeDisplayViewmodel
        binding.lifecycleOwner = this


        //VIEWS INITIALISATION
        setSupportActionBar(binding.mytoolbarBuildAlarm)
        binding.shapeCorner
        alarmName = findViewById(R.id.edit_name)
        binding.radioGroup
        val myRadioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        toggleOn = findViewById<RadioButton>(R.id.toggle_on)
        toggleOff = findViewById<RadioButton>(R.id.toggle_off)

        toggleOff.isChecked = true
        if(toggleOff.isChecked){
            toggleOn.isChecked = false
        }
        if(toggleOn.isChecked){
            toggleOff.isChecked = false
        }


        ///////////////////////// ALARM DAY CHOICES /////////////////////////////////////
        cbDay1 = findViewById(R.id.cb_day1)
        cbDay2 = findViewById(R.id.cb_day2)
        cbDay3 = findViewById(R.id.cb_day3)
        cbDay4 = findViewById(R.id.cb_day4)
        cbDay5 = findViewById(R.id.cb_day5)
        cbDay6 = findViewById(R.id.cb_day6)
        cbDay7 = findViewById(R.id.cb_day7)

        picker = findViewById(R.id.picker1)
        timeRangeCV = findViewById(R.id.time_range_picker)
        startTimeTv = findViewById(R.id.start_time_tv)
        endTimeTV = findViewById(R.id.end_time_tv)

        picker.sliderColor = Color.BLUE
        picker.clockFace = TimeRangePicker.ClockFace.APPLE
        picker.clockLabelColor = Color.WHITE
        picker.thumbSize = 100
        picker.thumbSizeActiveGrow = 1.0f
        picker.timeStepMinutes = 5
        picker.startTimeMinutes = 0
        picker.startTime = TimeRangePicker.Time(12,0)
        picker.endTime = TimeRangePicker.Time(7,0)
        startTimeTv.text = picker.startTime.toString()
        endTimeTV.text = picker.endTime.toString()

        picker.setOnDragChangeListener(object : TimeRangePicker.OnDragChangeListener {
            override fun onDragStart(thumb: TimeRangePicker.Thumb): Boolean {
                // Do something on start dragging
                if(thumb == TimeRangePicker.Thumb.START){
                    startTimeTv.textSize = 20.0f
                }
                if(thumb == TimeRangePicker.Thumb.END){
                    endTimeTV.textSize = 20.0f
                }
                return true // Return false to disallow the user from dragging a handle.
            }

            override fun onDragStop(thumb: TimeRangePicker.Thumb) {
                startTimeTv.textSize = 15.0f
                endTimeTV.textSize = 15.0f
            }
        })

        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                Log.d("TimeRangePicker", "Duration: " + duration)
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                endTimeTV.text = "End Time: ${endTime}"
            }

            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                Log.d("TimeRangePicker", "Start time: " + startTime)
                startTimeTv.text = "Start Time: ${startTime}"
            }
        })

        //////////////////////////////// END OF ALARM TIME LOGIC  //////////////////////////////////////////////////

        /////////////////////////////// INTERVAL TIME PICKER //////////////////////////////////////////////
        intervalPicker = findViewById(R.id.interval_picker)
        intervalPicker.minValue = 0
        intervalPicker.maxValue = 60
        intervalPicker.setFormatter {
            String.format("%02d", it)
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        currentRingtone = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)

        val ringtoneDefault = RingtoneManager.getRingtone(this, currentRingtone)
        val rt1 = ringtoneDefault.getTitle(this)
        Log.d("RT", rt1.toString())

        rt_tv = findViewById(R.id.ringtone_tv)
        rt_tv.text = "Current:\n"+rt1.toString()

       val playButton = findViewById<ImageButton>(R.id.play_button)
       val stopButton = findViewById<ImageButton>(R.id.stop_button)
       val chooseRingtone = findViewById<ImageButton>(R.id.choose_ringtone)

        playButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.play()
            }else{
                chosenRingtone!!.play()
            }
            playButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
        }
        stopButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.stop()
            }else{
                chosenRingtone!!.stop()
            }
            stopButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }

        var getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode == Activity.RESULT_OK){
                    chosenRTUri = it!!.data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    val chosenRingtone = RingtoneManager.getRingtone(this, chosenRTUri)
                    val rt1 = chosenRingtone.getTitle(this)
                    rt_tv.text = "Current:\n"+rt1.toString()
                }
            }

        chooseRingtone.setOnClickListener{
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentRingtone)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            getResult.launch(intent)
        }

        val saveButton = findViewById(R.id.save_button) as Button
        saveButton.setOnClickListener{
            insertNewAlarmToDB()
        }

    }////////////////////// END OF ON CREATE ///////////////////////////

    @InternalCoroutinesApi
    private fun insertNewAlarmToDB() {
//        var alarmDays = arrayListOf<String>()
//        var sb = StringBuilder()
        val usersAlarmName = if(alarmName != null) alarmName.text.toString() else "alarm"          //Alarm name
        //TODO("Got to get nuber of alarms in DB for this user and give the name plus num of alarms in DB to the name")
        var alarmDays = getCheckedDays()                                                            //Days Selected
//        var separator = ""
//        for (i in alarmDays.indices){
//            sb.append(separator+daysSelected[i])
//            separator = ","//MON,TUES
//        }
        var weekly = toggleOn.isChecked     //Is set to weekly?
        val startTime = picker.startTime.toString()   //07:00
        val endTime = picker.endTime.toString()    //08:00
        val ringtoneChosen:String = chosenRTUri.toString() ?: currentRingtone.toString()
        val interval = intervalPicker.value
        val time = cal.time.toString()
        val alarm = BuildNewAlarmModel(0, usersAlarmName, alarmDays, weekly, startTime, endTime, ringtoneChosen, interval, time)
        mAlarmViewModel.addAlarm(alarm)
        Toast.makeText(applicationContext, "Successfully Saved Your New Alarm", Toast.LENGTH_SHORT).show()

    }
    private fun inputCheck(usersAlarmName:String, alarmDays:ArrayList<String>, startTime:String, endTime:String, interval:Int): Boolean {
        return !(TextUtils.isEmpty(usersAlarmName) && alarmDays.isEmpty() && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && interval == null)
    }

    fun getCheckedDays(): String {
        daysSelected = arrayListOf()
        if(cbDay1.isChecked){
            Log.d("DaysCheccked: ", cbDay1.text.toString())
            daysSelected.add("Mon")
        }
        if(cbDay2.isChecked){
            Log.d("DaysCheccked: ", cbDay2.text.toString())
            daysSelected.add("Tue")
        }
        if(cbDay3.isChecked){
            Log.d("DaysCheccked: ", cbDay3.text.toString())
            daysSelected.add("Wed")
        }
        if(cbDay4.isChecked){
            Log.d("DaysCheccked: ", cbDay4.text.toString())
            daysSelected.add("Thu")
        }
        if(cbDay5.isChecked){
            Log.d("DaysCheccked: ", cbDay5.text.toString())
            daysSelected.add("Fri")
        }
        if(cbDay6.isChecked){
            Log.d("DaysCheccked: ", cbDay6.text.toString())
            daysSelected.add("Sat")
        }
        if(cbDay7.isChecked){
            Log.d("DaysCheccked: ", cbDay7.text.toString())
            daysSelected.add("Sun")
        }
        var separator = ""
        var sb = StringBuilder()
        for (i in daysSelected.indices){
            sb.append(separator+daysSelected[i])
            separator = ","//MON,TUES
        }
        return sb.toString()
    }

    fun formatTimeForDB(hour:Int, minute:Int): String {

        val h = "%02d".format(if (hour < 12) hour else hour - 12)
        val m = "%02d".format(minute)

        return "$h:$m"
    }
}
