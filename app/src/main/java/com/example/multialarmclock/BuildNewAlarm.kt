package com.example.multialarmclock

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.drawable.shapes.Shape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.util.rangeTo
import androidx.core.view.updateLayoutParams
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.example.multialarmclock.classes.BuildAlarmViewModel
import com.example.multialarmclock.classes.TimeViewModel
import com.example.multialarmclock.databinding.ActivityBuildNewAlarmBinding
import com.example.multialarmclock.databinding.ActivityMainBinding

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.internal.ViewUtils.dpToPx
import com.ramotion.fluidslider.FluidSlider
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class BuildNewAlarm : AppCompatActivity() {

    private lateinit var binding: ActivityBuildNewAlarmBinding

    @SuppressLint("LongLogTag", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildNewAlarmBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_build_new_alarm)

        setSupportActionBar(binding.mytoolbarBuildAlarm)

        binding.fluidSlider
        binding.shapeCorner
        binding.radioGroup
        binding.toggleOn
        binding.toggleOff
        binding.radioGroupTv

        val startButton = findViewById<Button>(R.id.open_time_picker_start)
        val endButton = findViewById<Button>(R.id.open_time_picker_end)

        val startTimeTv = findViewById<TextView>(R.id.start_time_tv)
        val endTimeTv = findViewById<TextView>(R.id.end_time_tv)

        startButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                startTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetlistener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        endButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetlistener = TimePickerDialog.OnTimeSetListener{
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                endTimeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetlistener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val every_cv = findViewById<CardView>(R.id.new_cardview)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            50
        )

        val everyRadio = findViewById<CheckBox>(R.id.every_radio)
        val slider = findViewById<FluidSlider>(R.id.fluid_slider)

        //Fluid slider for choosing the frequency of the alarm
        val max = 24.00
        val min = 00.00
        val total = max - min


//        val l = every_cv.layoutParams
//        l.height = dpToPx(this, 150).toInt()


        slider.positionListener = { pos -> slider.bubbleText = "${min + (100  * pos).toInt()}" }
        slider.position = 0.02f
        slider.startText ="Mins"
        slider.endText = "Hours"


        everyRadio.setOnCheckedChangeListener{_, every_radio ->
            Log.d("Testing","Radio Button Checked")
            slider.visibility = View.VISIBLE
            every_cv.updateLayoutParams {
                height = dpToPx(applicationContext, 150).toInt()
            }
        }
//        if(everyRadio.isChecked()){
//            slider.visibility = View.VISIBLE
//            every_cv.updateLayoutParams {
//                height = dpToPx(applicationContext, 150).toInt()
//            }
//        }else{
//            slider.visibility = View.INVISIBLE
//            every_cv.updateLayoutParams {
//                height = dpToPx(applicationContext, 50).toInt()
//            }
//        }
//        switch(everyRadio.id){
//            case R.id.every_radio
//        }
//        fun onCheckboxChecked(checkBox: CheckBox){
//            if(checkBox is CheckBox){
//                val checked: Boolean = checkBox.isChecked
//
//                when(checkBox.id){
//                    R.id.every_radio->{
//                        if(checked){
//                            slider.visibility = View.VISIBLE
//                            every_cv.updateLayoutParams {
//                                height = dpToPx(applicationContext, 150).toInt()
//                            }
//                        }else{
//                            slider.visibility = View.INVISIBLE
//                            every_cv.updateLayoutParams {
//                                height = dpToPx(applicationContext, 50).toInt()
//                            }
//                        }
//                    }
//                }
//            }
//        }


    }
}
