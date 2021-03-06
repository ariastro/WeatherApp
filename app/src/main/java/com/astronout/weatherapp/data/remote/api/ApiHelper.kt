package com.astronout.weatherapp.data.remote.api

import com.astronout.weatherapp.ui.main.model.GetWeatherResponseModel
import retrofit2.Response

interface ApiHelper {

    suspend fun getWeather(latitude: String, longitude: String, apiKey: String, units: String): Response<GetWeatherResponseModel>

}