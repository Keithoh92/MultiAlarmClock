package com.example.multialarmclock.feature.activity.alarmIntervalBuilder

import android.app.Activity
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.databinding.ActivityBuildNewAlarmBinding
import com.ramotion.fluidslider.FluidSlider
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [BuildIntervalAlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuildIntervalAlarmFragment : Fragment() {

    private lateinit var binding: ActivityBuildNewAlarmBinding
    @RequiresApi(Build.VERSION_CODES.P)
    internal lateinit var slider: FluidSlider

    private val viewModel by viewModel<BuildIntervalAlarmViewModel>()

    val cal = Calendar.getInstance()

    private lateinit var alarmName: EditText

    private lateinit var toggleOn: RadioButton
    private lateinit var toggleOff: RadioButton

    private lateinit var daysSelected:ArrayList<String>
    private lateinit var cbDay1: CheckBox
    private lateinit var cbDay2: CheckBox
    private lateinit var cbDay3: CheckBox
    private lateinit var cbDay4: CheckBox
    private lateinit var cbDay5: CheckBox
    private lateinit var cbDay6: CheckBox
    private lateinit var cbDay7: CheckBox

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var timeRangeCV: CardView

    private lateinit var startTimeTv: TextView
    private lateinit var endTimeTV: TextView
    private var startTimeTemp:String? = null
    private var endTimeTemp:String? = null

    private lateinit var rt_tv: TextView
    internal lateinit var ringtoneDefault: Ringtone
    private var chosenRingtone: Ringtone? = null
    private lateinit var currentRingtone: Uri
    var chosenRTUri: Uri?=null
    private lateinit var intervalPicker: NumberPicker


    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        toggleOff.isChecked = true
//        if(toggleOff.isChecked){
//            toggleOn.isChecked = false
//        }
//        if(toggleOn.isChecked){
//            toggleOff.isChecked = false
//        }

    }


    @OptIn(InternalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_alarm_interval_builder, container, false)

//        binding = ActivityBuildNewAlarmBinding.inflate(R.layout.fragment_alarm_interval_builder, container, false)
//        binding.shapeCorner
//        binding.radioGroup

        ////////////////////////Initialise Views///////////////////////////////////
        alarmName = root.findViewById(R.id.edit_name) //Alarm Name TextView
        //Alarm Weekly switch button
        toggleOn = root.findViewById<RadioButton>(R.id.toggle_on)
        toggleOff = root.findViewById<RadioButton>(R.id.toggle_off)

        //ALARM DAY CHOICES
        cbDay1 = root.findViewById(R.id.cb_day1)
        cbDay2 = root.findViewById(R.id.cb_day2)
        cbDay3 = root.findViewById(R.id.cb_day3)
        cbDay4 = root.findViewById(R.id.cb_day4)
        cbDay5 = root.findViewById(R.id.cb_day5)
        cbDay6 = root.findViewById(R.id.cb_day6)
        cbDay7 = root.findViewById(R.id.cb_day7)
        timeRangeCV = root.findViewById(R.id.time_range_picker) //cardview for start &  end time pickers
        //Alarm to go off every X minutes
        intervalPicker = root.findViewById(R.id.interval_picker)
        intervalPicker.minValue = 0
        intervalPicker.maxValue = 60
        intervalPicker.setFormatter {
            String.format("%02d", it)
        }

        setupObserver()

        onClickTime(root)
//        ringtoneManager()
        //////////////////////////////////////////////////////////////////////////////////////////////

        currentRingtone = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)

        val ringtoneDefault = RingtoneManager.getRingtone(activity, currentRingtone)
        val rt1 = ringtoneDefault.getTitle(activity)
        Log.d("RT", rt1.toString())

        rt_tv = root.findViewById(R.id.ringtone_tv)
        rt_tv.text = "Current:\n"+rt1.toString()

        val playButton = root.findViewById<ImageButton>(R.id.play_button)
        val stopButton = root.findViewById<ImageButton>(R.id.stop_button)
        val chooseRingtone = root.findViewById<ImageButton>(R.id.choose_ringtone)

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
                    val chosenRingtone = RingtoneManager.getRingtone(activity, chosenRTUri)
                    val rt1 = chosenRingtone.getTitle(activity)
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

        val saveButton = root.findViewById(R.id.save_button) as Button
        saveButton.setOnClickListener{
            if( startTimeTemp != null && endTimeTemp != null) {
                insertNewAlarmToDB()
            } else {
                Toast.makeText(activity, "You have not selected a Alarm StartTime Yet", Toast.LENGTH_LONG).show()
            }
        }

        return root
    }

    private fun setupObserver() {
        viewModel.onSuccess.observe(viewLifecycleOwner) { onSuccess ->
            if (onSuccess.toInt() != -1) {
                Toast.makeText(activity, "Successfully Saved Your New Alarm", Toast.LENGTH_SHORT).show()
                activity?.finish()
            } else {
                Toast.makeText(activity, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun ringtoneManager() {
//        TODO("Not yet implemented")
//    }

    companion object {

        @JvmStatic
        fun newInstance() = BuildIntervalAlarmFragment()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @InternalCoroutinesApi
    private fun insertNewAlarmToDB() {

        val newAlarm = buildNewAlarm()
        Log.d("BuildIntervalAlarm", "newAlarmData = $newAlarm")

        viewModel.addAlarm(newAlarm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun buildNewAlarm(): BuildNewAlarmDao {
        return BuildNewAlarmDao(
            0,
            alarmName = if(alarmName.text.isNotEmpty()) alarmName.text.toString() else "alarm",
            //TODO("Got to get nuber of alarms in DB for this user and give the name plus num of alarms in DB to the name")
            daysSelected = getCheckedDays(),
            weekly = toggleOn.isChecked,
            startTime = if(startTimePicker.minute < 10) "${startTimePicker.hour}:0${startTimePicker.minute}" else "${startTimePicker.hour}:${startTimePicker.minute}",
            endTime = if(endTimePicker.minute < 10) "${endTimePicker.hour}:0${endTimePicker.minute}" else "${endTimePicker.hour}:${endTimePicker.minute}",
            sound = chosenRTUri.toString(),
            interval = intervalPicker.value,
            time = cal.time.toString(),
            active = true
        )
    }

    private fun inputCheck(usersAlarmName:String, alarmDays:ArrayList<String>, startTime:String, endTime:String, interval:Int): Boolean {
        return !(TextUtils.isEmpty(usersAlarmName) && alarmDays.isEmpty() && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && interval == null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onClickTime(root: View) {
        startTimePicker = root.findViewById(R.id.picker1)
        endTimePicker = root.findViewById(R.id.picker2)

        startTimePicker.hour = 8
        startTimePicker.minute = 0
        endTimePicker.hour = 9
        endTimePicker.minute = 0

        startTimeTv = root.findViewById(R.id.start_time_tv)
        endTimeTV = root.findViewById(R.id.end_time_tv)

        startTimeTv.text = "Start Time: 8:00 AM"
        endTimeTV.text = "End Time: 9:00 AM"

        startTimePicker.setIs24HourView(true)
        startTimePicker.setOnTimeChangedListener { _,  hour, minute -> var hour = hour
            var am_pm = ""
            //AM_PM Decider Logic
            when {hour == 0 -> { hour += 12
                am_pm = "AM"
            }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> { hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            val min1 = if (minute < 10) "00" else minute
            val startTimeMsg = "Start Time: $hour:$min1 $am_pm"
            startTimeTemp = "$hour:$min1 $am_pm"
            startTimeTv.text = startTimeMsg
        }

        endTimePicker.setIs24HourView(true)
        endTimePicker.setOnTimeChangedListener { _,  hour, minute -> var hour = hour
            var am_pm = ""
            //AM_PM Decider Logic
            when {hour == 0 -> { hour += 12
                am_pm = "AM"
            }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> { hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (endTimeTV != null) {
                val min1 = if (minute < 10) "00" else minute
                val endTimeMsg = "End Time: $hour:$min1 $am_pm"
                endTimeTemp = "$hour:$min1 $am_pm"
                endTimeTV.text = endTimeMsg
            }
        }
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