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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.multialarmclock.MainActivity
import com.example.multialarmclock.prefs


class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = emptyList<BuildNewAlarmModel>()
    private lateinit var day:String

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val editor = itemView.context.getSharedPreferences("MyAlarm", Context.MODE_PRIVATE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = alarmList[position]

        holder.itemView.findViewById<TextView>(R.id.row_id_tv).text = currentItem.id.toString()
        if(currentItem.alarmName == ""){
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = "Alarm "+currentItem.id.toString()
        }else{
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = currentItem.alarmName
        }

        holder.itemView.findViewById<TextView>(R.id.time_range).text = currentItem.startTime+"-"+currentItem.endTime
        holder.itemView.findViewById<TextView>(R.id.interval).text = "Interval: "+currentItem.interval.toString()+" mins"
        holder.itemView.findViewById<TextView>(R.id.days).text = currentItem.daysSelected

        val switchButton = holder.itemView.findViewById<SwitchCompat>(R.id.my_switch)

        val editor = holder.itemView.context.getSharedPreferences("MyAlarm", Context.MODE_PRIVATE)
        val checking = editor.getBoolean("$currentItem.id", false)
        if(checking){
            switchButton.isChecked
        }else{
            !switchButton.isChecked
        }
        switchButton.setOnCheckedChangeListener{buttonView, isChecked ->
            val editThisPref = editor.edit()
            if(isChecked){
                editThisPref.putBoolean("$currentItem.id", true)
                editThisPref.commit()
            }else{
                editThisPref.putBoolean("$currentItem.id", false)
                editThisPref.commit()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(alarm: List<BuildNewAlarmModel>){
        this.alarmList = alarm
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}