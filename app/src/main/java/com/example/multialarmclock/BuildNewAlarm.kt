package com.example.multialarmclock

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.example.multialarmclock.classes.AlarmViewModel
import com.example.multialarmclock.data.BuildNewAlarmModel
import com.example.multialarmclock.databinding.ActivityBuildNewAlarmBinding

import com.ramotion.fluidslider.FluidSlider
import kotlinx.coroutines.InternalCoroutinesApi
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

    val cal = Calendar.getInstance()

    internal lateinit var startTimeTv:TextView
    internal lateinit var endTimeTv:TextView
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

    internal lateinit var rt_tv:TextView
    internal lateinit var ringtoneDefault:Ringtone
    internal lateinit var chosenRingtone:Ringtone
    var currentRingtone:Uri = TODO()
    var chosenRTUri:Uri?=null
    internal lateinit var intervalPicker:NumberPicker

    internal var min: Double = 00.00
    internal var max: Double = 00.00

    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("LongLogTag", "RestrictedApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildNewAlarmBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_build_new_alarm)

        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

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

        //////////////////////////////////      ALARM TIME LOGIC  ///////////////////////////////////////
        val startButton = findViewById<Button>(R.id.open_time_picker_start)
        val endButton = findViewById<Button>(R.id.open_time_picker_end)

        startTimeTv = findViewById(R.id.start_time_tv)
        endTimeTv = findViewById(R.id.end_time_tv)

        val minute = 0
        startButton.setOnClickListener{
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                startTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
                endTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time.time.plus(1))

            }
            TimePickerDialog(this, timeSetlistener, 7, 0, true).show()
        }

        endButton.setOnClickListener{
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                endTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetlistener, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show()
        }

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
                chosenRingtone.play()
            }
            playButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
        }
        stopButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.stop()
            }else{
                chosenRingtone.stop()
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

    private fun insertNewAlarmToDB() {
        var alarmDays = arrayListOf<String>()

        val usersAlarmName = if(alarmName != null) alarmName.text.toString() else "alarm"          //Alarm name
        //TODO("Got to get nuber of alarms in DB for this user and give the name plus num of alarms in DB to the name")
        alarmDays = getCheckedDays()                                                            //Days Selected
        var weekly = toggleOn.isChecked                                                         //Is set to weekly?
        val startTime = startTimeTv.text                                                        //startTime
        val endTime = endTimeTv.text                                                            //EndTime
        val ringtoneChosen:Uri = chosenRTUri ?: currentRingtone     //Ringtone
        val interval = intervalPicker.value                                                     //Interval
        val time = cal.time
//        if(inputCheck(usersAlarmName, alarmDays, startTime, endTime, ))
        val alarm = BuildNewAlarmModel(0, usersAlarmName, alarmDays, weekly, startTime, endTime, ringtoneChosen, interval,
            time as Time
        )
    }
    private fun inputCheck(usersAlarmName:String, alarmDays:ArrayList<String>, startTime:String, endTime:String, interval:Int): Boolean {
        return !(TextUtils.isEmpty(usersAlarmName) && alarmDays.isEmpty() && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && interval == null)
    }


    private fun getCheckedDays(): ArrayList<String> {
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
        return daysSelected
    }
}
