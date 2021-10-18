package com.example.multialarmclock.list

import android.annotation.SuppressLint
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

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = emptyList<BuildNewAlarmModel>()
    private lateinit var day:String



    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = alarmList[position]

//        var days = currentItem.daysSelected.split(",").toTypedArray()

        holder.itemView.findViewById<TextView>(R.id.row_id_tv).text = currentItem.id.toString()
        if(currentItem.alarmName == ""){
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = "Alarm "+currentItem.id.toString()
        }else{
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = currentItem.alarmName
        }

        holder.itemView.findViewById<TextView>(R.id.time_range).text = currentItem.startTime+"-"+currentItem.endTime
        holder.itemView.findViewById<TextView>(R.id.interval).text = "Interval: "+currentItem.interval.toString()+" mins"
        holder.itemView.findViewById<TextView>(R.id.days).text = currentItem.daysSelected
        holder.itemView.findViewById<Switch>(R.id.my_switch).setBackgroundColor(Color.BLUE)
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