package com.example.multialarmclock

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.Canvas
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
import androidx.recyclerview.widget.ItemTouchHelper
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
    var toolbar: Toolbar? = null
    val adapter = com.example.multialarmclock.list.ListAdapter(this)

    //Setup for last used cardview
    private lateinit var daysChosen:TextView
    private lateinit var range:TextView
    private lateinit var interval:TextView
    private lateinit var setButton:Button
    private lateinit var editButton:Button
    private lateinit var switchButton:SwitchCompat

    private val swipeRefreshLayout:SwipeRefreshLayout by lazy {
        findViewById(R.id.swipe_refresh_layout)
    }

    private val recyclerView:RecyclerView by lazy {
        findViewById(R.id.recyclerview)
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

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        //SeparatorDecoration
        recyclerView.addItemDecoration(DividerItemDecoration(
            applicationContext, LinearLayoutManager.VERTICAL
        ))

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            reloadAdapter()
        }

        mAlarmModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        reloadAdapter()

        setOnItemTouchHelper()

        //1st card view - set last created alarm details to view
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

    private fun setOnItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            // the limit of swipe same as delete button in item 100dp
            private val limitScrollX = dpToPx(100f, this@MainActivity)
            private var currentScrollX= 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MIN_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if(dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }

                    if(isCurrentlyActive) {
                        // swipe with finger
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        }
                        else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    }
                    else {
                        // sqipe with auto animation
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(), 0)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if(viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                }
                else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }

            }



        }).apply {
            attachToRecyclerView(recyclerView)
        }

    }

    private fun dpToPx(dpValue: Float, context: Context): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }

    @InternalCoroutinesApi
    fun deleteAlarm(id: Int) {
        mAlarmModel.deleteAlarm(id)
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

    //fill list with DB alarm data
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

//    fun setItemTouchListener(){
//
//    }
}