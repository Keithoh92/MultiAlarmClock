package com.example.multialarmclock.feature.activity.alarmIntervalBuilder

import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.media.Ringtone
import android.media.RingtoneManager
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
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.databinding.FragmentAlarmIntervalBuilderBinding
import com.example.multialarmclock.feature.activity.alarmIntervalBuilder.utils.CheckedDays
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class BuildIntervalAlarmFragment : Fragment(), BuildIntervalAlarmViewModel.CreateNewAlarm {

    private lateinit var binding: FragmentAlarmIntervalBuilderBinding

    private val viewModel by viewModel<BuildIntervalAlarmViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    private val cal: Calendar = Calendar.getInstance()

    private var startTimeTemp:String? = null
    private var endTimeTemp:String? = null

    internal lateinit var ringtoneDefault: Ringtone
    private var chosenRingtone: Ringtone? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmIntervalBuilderBinding.inflate(layoutInflater)

        setupObserver()
        initialiseClickListeners()
        setupIntervalPicker()
        setupRingtoneManager()

        return binding.root
    }

    private fun setupRingtoneManager() {
        viewModel.currentRingtone = RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM)

        val ringtoneDefault = RingtoneManager.getRingtone(activity, viewModel.currentRingtone)
        val rt1 = ringtoneDefault.getTitle(activity)
        Log.d("RT", rt1.toString())

        binding.ringtoneTv.text = getString(R.string.ringtone_tv, rt1.toString())
    }

    private fun setupIntervalPicker() {
        binding.intervalPicker.minValue = 0
        binding.intervalPicker.maxValue = 60
        binding.intervalPicker.setFormatter { String.format("%02d", it) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initialiseClickListeners() {
        setupTimePickerListeners()

        binding.playButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.play()
            }else{
                chosenRingtone?.play()
            }
            binding.playButton.visibility = View.INVISIBLE
            binding.stopButton.visibility = View.VISIBLE
        }

        binding.stopButton.setOnClickListener{
            if(chosenRingtone == null){
                ringtoneDefault.stop()
            }else{
                chosenRingtone?.stop()
            }
            binding.stopButton.visibility = View.INVISIBLE
            binding.playButton.visibility = View.VISIBLE
        }

        val getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode == Activity.RESULT_OK){
                    viewModel.chosenRingtoneUri = it?.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    val chosenRingtone = RingtoneManager.getRingtone(activity, viewModel.chosenRingtoneUri)
                    val rt1 = chosenRingtone.getTitle(activity)
                    binding.ringtoneTv.text = getString(R.string.ringtone_tv, rt1.toString())
                }
            }

        binding.chooseRingtone.setOnClickListener{
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, viewModel.currentRingtone)
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupTimePickerListeners() {
        binding.startTimePicker.hour = 8
        binding.startTimePicker.minute = 0
        binding.endTimePicker.hour = 9
        binding.endTimePicker.minute = 0

        binding.startTimeTv.text = getString(R.string.start_time_tv)
        binding.endTimeTv.text = getString(R.string.end_time_tv)

        binding.startTimePicker.setIs24HourView(true)
        binding.startTimePicker.setOnTimeChangedListener { _,  hour, minute -> var selectedHour = hour

            val (formattedHour, formattedAmPm) = getTimeDisplayFormat(selectedHour)

            val min1 = if (minute < 10) "00" else minute
            val startTimeMsg = getString(R.string.start_time_msg_on_change, formattedHour.toString(), min1, formattedAmPm)
            startTimeTemp = getString(R.string.end_time_msg_on_change, formattedHour.toString(), min1, formattedAmPm)
            binding.startTimeTv.text = startTimeMsg
        }

        binding.endTimePicker.setIs24HourView(true)
        binding.endTimePicker.setOnTimeChangedListener { _,  hour, minute -> var selectedHour = hour

            val (formattedHour, formattedAmPm) = getTimeDisplayFormat(selectedHour)

            val min1 = if (minute < 10) "00" else minute
            val endTimeMsg = "End Time: $formattedHour:$min1 $formattedAmPm"
            endTimeTemp = "$formattedHour:$min1 $formattedAmPm"
            binding.endTimeTv.text = endTimeMsg
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
        viewModel.createCheckedDaysString(getCheckedDays())

        val newAlarm = BuildNewAlarmDao(
            0,
            alarmName = if(binding.editName.text.isNotEmpty()) binding.editName.text.toString() else "alarm ${viewModel.getAlarmCount().plus(1)}",
            daysSelected = viewModel.daysSelectedDisplayString,
            weekly = binding.toggleOn.isChecked,
            startTime = if(binding.startTimePicker.minute < 10) "${binding.startTimePicker.hour}:0${binding.startTimePicker.minute}" else "${binding.startTimePicker.hour}:${binding.startTimePicker.minute}",
            endTime = if(binding.endTimePicker.minute < 10) "${binding.endTimePicker.hour}:0${binding.endTimePicker.minute}" else "${binding.endTimePicker.hour}:${binding.endTimePicker.minute}",
            sound = viewModel.chosenRingtoneUri.toString(),
            interval = binding.intervalPicker.value,
            time = cal.time.toString(),
            active = true
        )

        viewModel.createNewAlarm(newAlarm)
    }

    private fun inputCheck(usersAlarmName:String, alarmDays:ArrayList<String>, startTime:String, endTime:String, interval:Int): Boolean {
        return !(TextUtils.isEmpty(usersAlarmName) && alarmDays.isEmpty() && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && interval == null)
    }

    private fun getTimeDisplayFormat(selectedHour: Int): Pair<Int, String> {
        var amPm = ""
        var hour = 0
        when {selectedHour == 0 -> {
            hour += 12
            amPm = "AM"
        }
            hour == 12 -> amPm = "PM"
            hour > 12 -> {
                hour -= 12
                amPm = "PM"
            }
            else -> amPm = "AM"
        }
        return Pair(hour, amPm)
    }

    private fun getCheckedDays(): CheckedDays {
        return CheckedDays(
            monday = binding.cbDay1.isChecked,
            tuesday = binding.cbDay2.isChecked,
            wednesday = binding.cbDay3.isChecked,
            thursday = binding.cbDay4.isChecked,
            friday = binding.cbDay5.isChecked,
            saturday = binding.cbDay6.isChecked,
            sunday = binding.cbDay7.isChecked
        )
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

    override fun getCheckedDaysTestInterface(): String {
        TODO("Not yet implemented")
    }
}