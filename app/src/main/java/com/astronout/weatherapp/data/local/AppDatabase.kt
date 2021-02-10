package com.astronout.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.astronout.weatherapp.data.local.dao.CurrentWeatherDao
import com.astronout.weatherapp.data.local.entity.CurrentWeather
import com.astronout.weatherapp.utils.Constants.DATABASE_NAME

@Database(entities = [CurrentWeather::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}