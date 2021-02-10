package com.astronout.weatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronout.weatherapp.data.local.entity.CurrentWeather
import com.astronout.weatherapp.data.local.repository.LocalRepository
import com.astronout.weatherapp.data.remote.repository.RemoteRepository
import com.astronout.weatherapp.ui.main.model.GetWeatherResponseModel
import com.astronout.weatherapp.utils.Constants.NO_INTERNET_CONNECTION
import com.astronout.weatherapp.utils.NetworkHelper
import com.astronout.weatherapp.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository, private val networkHelper: NetworkHelper) : ViewModel() {

    private val _weatherReponse = MutableLiveData<Resource<GetWeatherResponseModel>>()
    val weatherReponse: LiveData<Resource<GetWeatherResponseModel>>
        get() = _weatherReponse

    val currentWeather = localRepository.getCurrentWeather

    fun upsertWeather(weather: CurrentWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.upsertWeather(weather)
        }
    }

    fun getWeather(latitude: String, longitude: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherReponse.postValue(Resource.loading(null))
            try {
                remoteRepository.getWeather(latitude, longitude).let {
                    if (it.isSuccessful) {
                        _weatherReponse.postValue(Resource.success(it.body()))
                    } else {
                        _weatherReponse.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } catch (e: Exception) {
                _weatherReponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }
}