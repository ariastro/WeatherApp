package com.astronout.weatherapp.data.remote.api

import com.astronout.weatherapp.ui.main.model.GetWeatherResponseModel
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getWeather(city: String, apiKey: String, units: String): Response<GetWeatherResponseModel> =
        apiService.getWeather(city, apiKey, units)

}