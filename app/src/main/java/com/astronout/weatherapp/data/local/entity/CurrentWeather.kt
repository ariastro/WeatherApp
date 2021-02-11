package com.astronout.weatherapp.data.local.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.astronout.weatherapp.utils.Constants.WEATHER_ID

@Keep
@Entity(tableName = "current_weather")
data class CurrentWeather (
    @PrimaryKey (autoGenerate = false)
    val id: Int = WEATHER_ID,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: String,
    @ColumnInfo(name = "cloud")
    val cloud: String,
    @ColumnInfo(name = "humidity")
    val humidity: String,
    @ColumnInfo(name = "temp")
    val temp: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "weather")
    val weather: String,
    @ColumnInfo(name = "weather_desc")
    val weatherDesc: String
)