package com.example.multialarmclock.feature.activity.homeScreen

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmDao
import com.example.multialarmclock.databinding.ActivityMainBinding
import com.example.multialarmclock.feature.activity.alarmIntervalBuilder.BuildIntervalAlarmActivity
import kotlinx.coroutines.InternalCoroutinesApi
import com.example.multialarmclock.feature.activity.homeScreen.AlarmsListAdapter.ListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<HomeScreenViewModel>()

    private var menu: Menu? = null

    private val adapter = ListAdapter (
        { id ->
            viewModel.deleteAlarm(id)
        },
        { id, active ->
            viewModel.updateActiveState(id, active)
        }
    )

    @OptIn(InternalCoroutinesApi::class)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.mytoolbar)

        initialiseRecyclerView()
        initialiseViewModel()
        initialiseClickListeners()
        setupObservers()
        setOnItemTouchHelper()
    }

    override fun onResume() {
        super.onResume()
        initialiseViewModel()
    }

    private fun initialiseRecyclerView() {
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        addSeparatorDecorationToRecyclerView()
    }

    private fun initialiseClickListeners() {
        binding.cardviewBottomRight.setOnClickListener { openAlarmBuilder() }
    }

    private fun setupObservers() {
        viewModel.readAllData.observe(this) { alarm ->
            adapter.setData(alarm)
        }

        viewModel.readLastEntered.observe(this) { alarm ->
            populateFirstCardViewWithLastCreatedAlarm(alarm)
        }
    }

    private fun populateFirstCardViewWithLastCreatedAlarm(lastCreatedAlarm: List<BuildNewAlarmDao>) {
        if(lastCreatedAlarm.isNotEmpty()) {
            val daysSelected = lastCreatedAlarm.first().daysSelected
            val startTime = lastCreatedAlarm.first().startTime
            val endTime = lastCreatedAlarm.first().endTime
            val interval = lastCreatedAlarm.first().interval
            val isActive = lastCreatedAlarm.first().active

            binding.daysChosen.text = daysSelected
            binding.onOffSignal.text = if (isActive) getString(R.string.on) else getString(R.string.off)
            binding.range.text = getString(R.string.range_tv, startTime, endTime)

            binding.interval.text = if (interval == 60) {
                getString(R.string.interval_default_tv)
            } else {
                getString(R.string.interval_tv, interval.toString())
            }
            Log.d("MainAct", daysSelected)
        }else {
            binding.daysChosen.text = getString(R.string.no_alarms)
            binding.range.text = getString(R.string.range_not_available)
            binding.interval.text = getString(R.string.not_available)
        }
    }

    private fun initialiseViewModel() {
        viewModel.getAllAlarms()
        viewModel.getLastSavedAlarm()
    }

    private fun addSeparatorDecorationToRecyclerView() {
        binding.recyclerview.addItemDecoration(DividerItemDecoration(
            applicationContext, LinearLayoutManager.VERTICAL
        ))
    }

    private fun setOnItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            // the limit of swipe same as delete button in item 100dp
            private val limitScrollX = dpToPx(100f, this@HomeActivity)
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
                        // swipe with auto animation
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
            attachToRecyclerView(binding.recyclerview)
        }
    }

    private fun dpToPx(dpValue: Float, context: Context): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }

    private fun openAlarmBuilder() {
        val startAlarmBuilderActivity = Intent(this, BuildIntervalAlarmActivity::class.java)
        startActivity(startAlarmBuilderActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }
}