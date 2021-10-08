package com.example.multialarmclock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [BuildNewAlarmModel::class], version = 1, exportSchema = false)
abstract class AlarmDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object{
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context:Context):AlarmDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}