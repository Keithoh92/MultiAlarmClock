package com.example.multialarmclock

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.multialarmclock.classes.AlarmViewModel
import com.example.multialarmclock.data.AlarmDao
import com.example.multialarmclock.data.AlarmDatabase
import com.example.multialarmclock.data.BuildNewAlarmModel
import com.example.multialarmclock.databinding.ActivityMainBinding
import kotlinx.coroutines.InternalCoroutinesApi
import android.widget.ListAdapter as ListAdapter


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    @InternalCoroutinesApi
    private lateinit var mAlarmModel: AlarmViewModel
    var settings: ImageButton? = null
    var avd: AnimatedVectorDrawableCompat? = null
    var avd2: AnimatedVectorDrawable? = null
    var toolbar: Toolbar? = null

    private lateinit var cursor:Cursor

    internal lateinit var cvBottomRight:CardView
//    var radius resources

    private var menu: Menu? = null
    @InternalCoroutinesApi
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.mytoolbar
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = com.example.multialarmclock.list.ListAdapter()
//        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        mAlarmModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmModel.readAllData.observe(this, Observer { alarm->
            adapter.setData(alarm)
        })

        val daysChosen = findViewById<TextView>(R.id.days_chosen)
        val range = findViewById<TextView>(R.id.range)
        val interval = findViewById<TextView>(R.id.go_off_times)

//        val fs = mAlarmModel.readLastEntered
//        mAlarmModel.readLastEntered.observe(this, Observer { alarm->
//            daysChosen.text = alarm.toString()
//            range.text = alarm.get(4).toString()+alarm.get(5).toString()
//            interval.text = alarm.get(7).toString()

//            Log.d("DB testing", alarm.get(0).alarmName.toString())
//            Log.d("DB testing", alarm.last().toString())
//        })

//        val db = this.mAlarmModel
//
//        cursor = mAlarmModel.readLastEntered
//        if(cursor.moveToFirst()){
//            do{
//                daysChosen.text = cursor.columnNames(mAlarmModel.readLastEntered)
//            }
//        }

//        try{
//            cursor = db.readLastEntered.observe(this, Observer { cursor->
//                cursor.get(0).alarmName
//            })
//        }

//        val db:SQLiteDatabase
//        db.get



        val switchButton = findViewById<Switch>(R.id.my_switch)

        intitialiseFields();

        cvBottomRight.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                openAlarmBuilder()
            }
        })


    }

    private fun openAlarmBuilder() {
        val startAlarmBuilderActivity = Intent(this, BuildNewAlarm::class.java)
        startActivity(startAlarmBuilderActivity)
    }

    private fun intitialiseFields() {
        val cardviewTopLeft = findViewById<CardView>(R.id.cardview_top_left)
        val cardviewTopRight = findViewById<CardView>(R.id.cardview_top_right)
        val cardviewBottomLeft = findViewById<CardView>(R.id.cardview_top_right)
        cvBottomRight = findViewById(R.id.cardview_bottom_right)


        binding.cardviewTopLeftTv
        binding.topRightCardviewTv
        binding.cardviewBottomLeftTv
        binding.cardviewBottomRightTv

        binding.setButton
        binding.editButton
        binding.daysChosen
        binding.divider
        binding.divider2
        binding.range
        binding.goOffTimes
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawable = item.icon
        if (drawable is AnimatedVectorDrawable) {
            avd = drawable as AnimatedVectorDrawableCompat
            avd!!.start()
        } else if (drawable is AnimatedVectorDrawable) {
            avd2 = drawable
            avd2!!.start()
        }
        return super.onOptionsItemSelected(item)
    }

//    fun buttonEffect(button: View){
//        cardviewEditButton.setOnTouchListener { v, event ->
//            when(event.action){
//                MotionEvent.ACTION_DOWN -> {
//                    v.background.setColorFilter()
//                }
//            }
//        }
//    }
}