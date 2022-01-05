package com.example.multialarmclock

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomOpenHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.multialarmclock.classes.AlarmViewModel
import com.example.multialarmclock.data.AlarmDao
import com.example.multialarmclock.data.AlarmDatabase
import com.example.multialarmclock.data.BuildNewAlarmModel
import com.example.multialarmclock.databinding.ActivityMainBinding
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.properties.Delegates
import android.widget.ListAdapter as ListAdapter


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    @InternalCoroutinesApi
    private lateinit var mAlarmModel: AlarmViewModel
    var settings: ImageButton? = null
    var avd: AnimatedVectorDrawableCompat? = null
    var avd2: AnimatedVectorDrawable? = null
    var toolbar: Toolbar? = null
    val adapter = com.example.multialarmclock.list.ListAdapter()

    //Setup for last used cardview
    private lateinit var daysChosen:TextView
    private lateinit var range:TextView
    private lateinit var interval:TextView
    private lateinit var setButton:Button
    private lateinit var editButton:Button
    private lateinit var switchButton:SwitchCompat
    val myPrefs = HashMap<String, MutableLiveData<Boolean>>()

    private val swipeRefreshLayout:SwipeRefreshLayout by lazy {
        findViewById(R.id.swipe_refresh_layout)
    }


    private lateinit var cursor:Cursor
    private lateinit var dividerItemDecoration: DividerItemDecoration

    internal lateinit var cvBottomRight:CardView

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
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        SeperatorDecoration
        recyclerView.addItemDecoration(DividerItemDecoration(
            applicationContext, LinearLayoutManager.VERTICAL
        ))


        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            reloadAdapter()
        }

        mAlarmModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        reloadAdapter()


        mAlarmModel.readLastEntered.observe(this, Observer { alarm->
            daysChosen.text = alarm.get(0).daysSelected
            val st = alarm.get(0).startTime
            val et = alarm.get(0).endTime
            range.text = "Range: $st-$et"
            if(alarm.get(0).interval == 60){
                interval.text = "Set Every hour"
            }else{
                interval.text = "Set Every "+alarm.get(0).interval+" mins"
            }
            Log.d("MainAct", alarm.get(0).daysSelected)
        })

        intitialiseFields();

        cvBottomRight.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                openAlarmBuilder()
            }
        })

    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        reloadAdapter()
        val editor = getSharedPreferences("MyAlarms", Context.MODE_PRIVATE)
    }

    private fun openAlarmBuilder() {
        val startAlarmBuilderActivity = Intent(this, BuildNewAlarm::class.java)
        startActivity(startAlarmBuilderActivity)
    }

    @InternalCoroutinesApi
    private fun reloadAdapter(){
        mAlarmModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmModel.readAllData.observe(this, Observer { alarm->
            adapter.setData(alarm)
        })
    }

    private fun intitialiseFields() {
        val cardviewTopLeft = findViewById<CardView>(R.id.cardview_top_left)
        val cardviewTopRight = findViewById<CardView>(R.id.cardview_top_right)
        val cardviewBottomLeft = findViewById<CardView>(R.id.cardview_top_right)
        cvBottomRight = findViewById(R.id.cardview_bottom_right)

        daysChosen = findViewById<TextView>(R.id.days_chosen)
        range = findViewById<TextView>(R.id.range)
        interval = findViewById<TextView>(R.id.go_off_times)
        editButton = findViewById(R.id.edit_button)
        setButton = findViewById(R.id.set_button)

        binding.cardviewTopLeftTv
        binding.topRightCardviewTv
        binding.cardviewBottomLeftTv
        binding.cardviewBottomRight

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

    fun setItemTouchListener(){

    }
}