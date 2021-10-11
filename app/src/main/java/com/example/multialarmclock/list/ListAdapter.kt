package com.example.multialarmclock.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmModel
import java.lang.StringBuilder

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = emptyList<BuildNewAlarmModel>()


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = alarmList[position]

        var days = currentItem.daysSelected.split(",").toTypedArray()
        var sb = StringBuilder()
        var separator = ""
        for (i in days.indices){
            var day = days[i]
            Log.d("testing days again", day)
            var dayFirstLetter:String = day.substring(0,0)

            sb.append(separator+dayFirstLetter)
            separator = "-"
        }
        holder.itemView.findViewById<TextView>(R.id.row_id_tv).text = currentItem.id.toString()
        holder.itemView.findViewById<TextView>(R.id.alarm_name).text = "Name: "+currentItem.alarmName
        holder.itemView.findViewById<TextView>(R.id.time_range).text = currentItem.startTime+"-"+currentItem.endTime
        holder.itemView.findViewById<TextView>(R.id.interval).text = "Interval: "+currentItem.interval.toString()+" mins"
        holder.itemView.findViewById<TextView>(R.id.days).text = sb.toString()
        sb.clear()
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