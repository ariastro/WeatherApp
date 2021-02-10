package com.astronout.weatherapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.astronout.weatherapp.data.local.entity.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * from current_weather")
    fun getCurrentWeather(): LiveData<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeather(weather: CurrentWeather)

}