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
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.databinding.FragmentAlarmIntervalBuilderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class BuildIntervalAlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmIntervalBuilderBinding

    private val viewModel by viewModel<BuildIntervalAlarmViewModel>()

    private val cal: Calendar = Calendar.getInstance()

    private lateinit var daysSelected:ArrayList<String>

    private var startTimeTemp:String? = null
    private var endTimeTemp:String? = null

    internal lateinit var ringtoneDefault: Ringtone
    private var chosenRingtone: Ringtone? = null
    private lateinit var currentRingtone: Uri
    var chosenRTUri: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmIntervalBuilderBinding.inflate(layoutInflater)

        binding.intervalPicker.minValue = 0
        binding.intervalPicker.maxValue = 60
        binding.intervalPicker.setFormatter {
            String.format("%02d", it)
        }

        setupObserver()

        onClickTime(binding.root)

        currentRingtone = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)

        val ringtoneDefault = RingtoneManager.getRingtone(activity, currentRingtone)
        val rt1 = ringtoneDefault.getTitle(activity)
        Log.d("RT", rt1.toString())

        binding.ringtoneTv.text = "Current:\n"+rt1.toString()

        initialiseClickListeners()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initialiseClickListeners() {
        binding.playButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.play()
            }else{
                chosenRingtone!!.play()
            }
            binding.playButton.visibility = View.INVISIBLE
            binding.stopButton.visibility = View.VISIBLE
        }

        binding.stopButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.stop()
            }else{
                chosenRingtone!!.stop()
            }
            binding.stopButton.visibility = View.INVISIBLE
            binding.playButton.visibility = View.VISIBLE
        }

        var getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode == Activity.RESULT_OK){
                    chosenRTUri = it!!.data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    val chosenRingtone = RingtoneManager.getRingtone(activity, chosenRTUri)
                    val rt1 = chosenRingtone.getTitle(activity)
                    binding.ringtoneTv.text = "Current:\n"+rt1.toString()
                }
            }

        binding.chooseRingtone.setOnClickListener{
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentRingtone)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            getResult.launch(intent)
        }

        binding.saveButton.setOnClickListener{
            if( startTimeTemp != null && endTimeTemp != null) {
                insertNewAlarmToDB()
            } else {
                Toast.makeText(activity, "You have not selected a Alarm StartTime Yet", Toast.LENGTH_LONG).show()
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun insertNewAlarmToDB() {
        val newAlarm = buildNewAlarm()
        Log.d("BuildIntervalAlarm", "newAlarmData = $newAlarm")

        viewModel.addAlarm(newAlarm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun buildNewAlarm(): BuildNewAlarmDao {
        return BuildNewAlarmDao(
            0,
            alarmName = if(binding.editName.text.isNotEmpty()) binding.editName.text.toString() else "alarm ${viewModel.getAlarmCount().plus(1)}",
            daysSelected = getCheckedDays(),
            weekly = binding.toggleOn.isChecked,
            startTime = if(binding.startTimePicker.minute < 10) "${binding.startTimePicker.hour}:0${binding.startTimePicker.minute}" else "${binding.startTimePicker.hour}:${binding.startTimePicker.minute}",
            endTime = if(binding.endTimePicker.minute < 10) "${binding.endTimePicker.hour}:0${binding.endTimePicker.minute}" else "${binding.endTimePicker.hour}:${binding.endTimePicker.minute}",
            sound = chosenRTUri.toString(),
            interval = binding.intervalPicker.value,
            time = cal.time.toString(),
            active = true
        )
    }

    private fun inputCheck(usersAlarmName:String, alarmDays:ArrayList<String>, startTime:String, endTime:String, interval:Int): Boolean {
        return !(TextUtils.isEmpty(usersAlarmName) && alarmDays.isEmpty() && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && interval == null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onClickTime(root: View) {

        binding.startTimePicker.hour = 8
        binding.startTimePicker.minute = 0
        binding.endTimePicker.hour = 9
        binding.endTimePicker.minute = 0

        binding.startTimeTv.text = "Start Time: 8:00 AM"
        binding.endTimeTv.text = "End Time: 9:00 AM"

        binding.startTimePicker.setIs24HourView(true)
        binding.startTimePicker.setOnTimeChangedListener { _,  hour, minute -> var hour = hour
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
            binding.startTimeTv.text = startTimeMsg
        }

        binding.endTimePicker.setIs24HourView(true)
        binding.endTimePicker.setOnTimeChangedListener { _,  hour, minute -> var hour = hour
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
            val endTimeMsg = "End Time: $hour:$min1 $am_pm"
            endTimeTemp = "$hour:$min1 $am_pm"
            binding.endTimeTv.text = endTimeMsg
        }
    }

    fun getCheckedDays(): String {
        daysSelected = arrayListOf()
        if(binding.cbDay1.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay1.text.toString())
            daysSelected.add("Mon")
        }
        if(binding.cbDay2.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay2.text.toString())
            daysSelected.add("Tue")
        }
        if(binding.cbDay3.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay3.text.toString())
            daysSelected.add("Wed")
        }
        if(binding.cbDay4.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay4.text.toString())
            daysSelected.add("Thu")
        }
        if(binding.cbDay5.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay5.text.toString())
            daysSelected.add("Fri")
        }
        if(binding.cbDay6.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay6.text.toString())
            daysSelected.add("Sat")
        }
        if(binding.cbDay7.isChecked){
            Log.d("DaysCheccked: ", binding.cbDay7.text.toString())
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

    companion object {
        @JvmStatic
        fun newInstance() = BuildIntervalAlarmFragment()
    }
}