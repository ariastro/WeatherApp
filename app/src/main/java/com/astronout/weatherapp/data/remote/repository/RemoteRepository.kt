package com.astronout.weatherapp.data.remote.repository

import com.astronout.weatherapp.BuildConfig
import com.astronout.weatherapp.data.remote.api.ApiHelper
import com.astronout.weatherapp.utils.Constants.METRIC

class RemoteRepository(private val apiHelper: ApiHelper) {

    suspend fun getWeather(latitude: String, longitude: String) = apiHelper.getWeather(latitude, longitude, BuildConfig.API_KEY, METRIC)

}