package com.example.multialarmclock.feature.activity.homeScreen.AlarmsListAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multialarmclock.R
import com.example.multialarmclock.data.BuildNewAlarmDao

import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import java.lang.ref.WeakReference

class ListAdapter(private val clickListener: (id: Int) -> Unit, private val switchListener: (id: Int, active: Boolean) -> Unit ): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = mutableListOf<BuildNewAlarmDao>()

    inner class MyViewHolder(itemView: View, private val clickListener: (id: Int) -> Unit, private val switchListener: (id: Int, active: Boolean) -> Unit ):RecyclerView.ViewHolder(itemView){
        private val view = WeakReference(itemView)
        private lateinit var id:TextView
        private lateinit var name:TextView
        private lateinit var timeRange:TextView
        private lateinit var interval:TextView
        private lateinit var days:TextView
        private lateinit var mListener: AdapterView.OnItemClickListener
        private lateinit var textviewDelete: TextView
        private lateinit var switchButton: SwitchCompat

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
                switchButton = it.findViewById(R.id.my_switch)

                textviewDelete.setOnClickListener {

                    onDeleteClick?.let { onDeleteClick ->
                        Log.d("ListAdapter", "onDeleteClick this used")
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
            clickListener,
            switchListener
        )

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
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = "Alarm ${currentItem.id}"
        }else{
            holder.itemView.findViewById<TextView>(R.id.alarm_name).text = currentItem.alarmName
        }
        holder.itemView.findViewById<TextView>(R.id.time_range).text = "${currentItem.startTime}-${currentItem.endTime}"
        holder.itemView.findViewById<TextView>(R.id.interval).text = "Interval: ${currentItem.interval} mins"
        holder.itemView.findViewById<TextView>(R.id.days).text = currentItem.daysSelected

        val switchButton = holder.itemView.findViewById<SwitchCompat>(R.id.my_switch)
        switchButton.isChecked = currentItem.active

        switchButton.setOnCheckedChangeListener{ _, isChecked ->
            switchListener.invoke(currentItem.id, isChecked)
        }

    }

    private fun removeItem(it: RecyclerView.ViewHolder) {
        val position = it.adapterPosition
        alarmList.removeAt(position) //remove data
        notifyItemRemoved(position) //remove item
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(alarm: List<BuildNewAlarmDao>){
        this.alarmList.clear()
        this.alarmList = alarm as MutableList<BuildNewAlarmDao>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

}