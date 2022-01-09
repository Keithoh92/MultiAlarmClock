package com.example.multialarmclock.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmModel
import java.lang.StringBuilder
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.multialarmclock.MainActivity
import com.example.multialarmclock.classes.AlarmViewModel
import com.example.multialarmclock.prefs
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.ref.WeakReference


class ListAdapter(private val clickListener: (id: Int) -> Unit ): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = mutableListOf<BuildNewAlarmModel>()
    private lateinit var day:String
    private var id: Int = 0

    inner class MyViewHolder(itemView: View, private val clickListener: (id: Int) -> Unit ):RecyclerView.ViewHolder(itemView){
        val editor = itemView.context.getSharedPreferences("MyAlarm", Context.MODE_PRIVATE)
        private val view = WeakReference(itemView)
        private lateinit var id:TextView
        private lateinit var name:TextView
        private lateinit var timeRange:TextView
        private lateinit var interval:TextView
        private lateinit var days:TextView
        private lateinit var mListener: AdapterView.OnItemClickListener;
        private lateinit var textviewDelete: TextView

        var index = 0
        var onDeleteClick:((RecyclerView.ViewHolder) -> Unit )? = null

        init {
            Log.d("ListAdapter", "Initialising fields")
            view.get()?.let {
                it.setOnClickListener{
                    if (view.get()?.scrollX != 0){
                        view.get()?.scrollTo(0, 0)
                    }
                }

                id = it.findViewById(R.id.row_id_tv)
                name = it.findViewById(R.id.alarm_name)
                timeRange = it.findViewById(R.id.time_range)
                interval = it.findViewById(R.id.interval)
                days = it.findViewById(R.id.days)
                textviewDelete = it.findViewById(R.id.textview_delete)

                textviewDelete.setOnClickListener {

                    onDeleteClick?.let { onDeleteClick ->
                        onDeleteClick(this)
                    }
                }
            }
        }

        fun updateView() {
            view.get()?.scrollTo(0, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row2, parent, false),
            clickListener
        )


    @InternalCoroutinesApi
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = alarmList[position]

        holder.updateView()

        holder.onDeleteClick = {
            removeItem(it)
            clickListener.invoke(currentItem.id)
        }

        holder.itemView.findViewById<TextView>(R.id.row_id_tv).text = currentItem.id.toString()
        if(currentItem.alarmName == ""){
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = "Alarm "+currentItem.id.toString()
        }else{
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = currentItem.alarmName
        }

        holder.itemView.findViewById<TextView>(R.id.time_range).text = currentItem.startTime+"-"+currentItem.endTime
        holder.itemView.findViewById<TextView>(R.id.interval).text = "Interval: "+currentItem.interval.toString()+" mins"
        holder.itemView.findViewById<TextView>(R.id.days).text = currentItem.daysSelected
        Log.d("Testing Adapter", "${currentItem.daysSelected}")
        val switchButton = holder.itemView.findViewById<SwitchCompat>(R.id.my_switch)

        val sharedPreferences = holder.itemView.context.getSharedPreferences("MyAlarms", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        switchButton.isChecked = sharedPreferences.getBoolean("$currentItem.id", false)
        Log.d("Alarm No.: ", currentItem.id.toString())

        switchButton.setOnCheckedChangeListener{ _, isChecked ->
            Log.d("Toggled Alarm no: ", currentItem.id.toString())
            if(isChecked){
                editor.putBoolean("$currentItem.id", true)
            }else{
                editor.putBoolean("$currentItem.id", false)
            }
            editor.commit()
        }
    }

    private fun removeItem(it: RecyclerView.ViewHolder) {
        val position = it.adapterPosition
        alarmList.removeAt(position) //remove data
        notifyItemRemoved(position) //remove item
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(alarm: List<BuildNewAlarmModel>){
        this.alarmList.clear()
        this.alarmList = alarm as MutableList<BuildNewAlarmModel>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    interface CallbackInterface {
        fun passBackIdToBeDeleted(id: Int)
    }

}