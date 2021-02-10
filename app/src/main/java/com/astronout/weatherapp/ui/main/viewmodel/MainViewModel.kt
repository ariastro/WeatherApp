package com.astronout.weatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronout.weatherapp.data.remote.repository.RemoteRepository
import com.astronout.weatherapp.ui.main.model.GetWeatherResponseModel
import com.astronout.weatherapp.utils.Constants.NO_INTERNET_CONNECTION
import com.astronout.weatherapp.utils.NetworkHelper
import com.astronout.weatherapp.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RemoteRepository, private val networkHelper: NetworkHelper): ViewModel() {

    private val _weather = MutableLiveData<Resource<GetWeatherResponseModel>>()
    val weather: LiveData<Resource<GetWeatherResponseModel>>
        get() = _weather

    fun getWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                repository.getWeather(city).let {
                    if (it.isSuccessful) {
                        _weather.postValue(Resource.success(it.body()))
                    } else {
                        _weather.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else {
                _weather.postValue(Resource.error(NO_INTERNET_CONNECTION, null))
            }
        }
    }

}