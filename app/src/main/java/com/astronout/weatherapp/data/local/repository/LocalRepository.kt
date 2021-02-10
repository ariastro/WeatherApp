package com.astronout.weatherapp.data.local.repository

import com.astronout.weatherapp.data.local.AppDatabase
import com.astronout.weatherapp.data.local.entity.CurrentWeather

class LocalRepository(private val appDatabase: AppDatabase) {

    val getCurrentWeather = appDatabase.currentWeatherDao().getCurrentWeather()

    suspend fun upsertWeather(currentWeather: CurrentWeather) {
        appDatabase.currentWeatherDao().upsertWeather(currentWeather)
    }

}