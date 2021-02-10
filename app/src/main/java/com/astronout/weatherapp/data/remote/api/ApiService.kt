package com.astronout.weatherapp.data.remote.api

import com.astronout.weatherapp.ui.main.model.GetWeatherResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("weather")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("appid") apiKey: String,
                           @Query("units") units: String) : Response<GetWeatherResponseModel>

}