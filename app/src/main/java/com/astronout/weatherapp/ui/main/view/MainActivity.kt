package com.astronout.weatherapp.ui.main.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astronout.weatherapp.R
import com.astronout.weatherapp.base.BaseActivity
import com.astronout.weatherapp.databinding.ActivityMainBinding
import com.astronout.weatherapp.ui.main.viewmodel.MainViewModel
import com.astronout.weatherapp.utils.*
import com.astronout.weatherapp.utils.Constants.DATE_FORMAT
import com.astronout.weatherapp.utils.glide.GlideApp
import com.astronout.weatherapp.vo.Status
import com.bumptech.glide.GenericTransitionOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val REQUEST_CODE_PERMISSION = 1252
    private val REQUEST_CODE_GPS = 51

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupSwipeRefreshLayout()
        checkLocationPermission()
        getCurrentDate()
        setupDayNight()
        observeWeather()

    }

    private fun getCurrentWeather(latitude: String, longitude: String) {
        viewModel.getWeather(latitude, longitude)
    }

    private fun checkLocationPermission() {
        if (hasPermissionLocation(this, REQUEST_CODE_PERMISSION)) {
            checkLocationServices()
        }
    }

    private fun checkLocationServices() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener(this) {
            getDeviceLocation()
        }

        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, REQUEST_CODE_GPS)
                } catch (e1: IntentSender.SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                if (result != null) {
                    getCurrentWeather(
                        task.result.latitude.toString(),
                        task.result.longitude.toString()
                    )
                } else {
                    val locationRequest = LocationRequest.create()
                    locationRequest.interval = 5000
                    locationRequest.fastestInterval = 5000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            super.onLocationResult(locationResult)
                            if (locationResult == null) {
                                return
                            }
                            val location = locationResult.lastLocation
                            getCurrentWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                            fusedLocationClient.removeLocationUpdates(locationCallback)
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
            } else {
                showWarningToasty(getString(R.string.failed_to_get_location))
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getDeviceLocation()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getCurrentDate() {
        val currentDate = DateTime.now()
        binding.date.text = currentDate.toString(DATE_FORMAT)
    }

    private fun setupDayNight() {
        when (DateTime().hourOfDay().get()) {
            in 0..16 -> {
                binding.parent.background = ContextCompat.getDrawable(this, R.drawable.morning)
                window.statusBarColor = ContextCompat.getColor(this, R.color.dayStatusBarColor)
            }
            in 17..24 -> {
                binding.parent.background = ContextCompat.getDrawable(this, R.drawable.night)
                window.statusBarColor = ContextCompat.getColor(this, R.color.nightStatusBarColor)
            }
        }
    }

    private fun observeWeather() {
        viewModel.weather.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progress.dismiss()
                    binding.cloud.text = getString(
                        R.string.cloud_percentage,
                        it.data!!.clouds.all.toString()
                    )
                    binding.windSpeed.text = getString(
                        R.string.wind_speed,
                        it.data.wind.speed.toString()
                    )
                    binding.humidity.text = getString(
                        R.string.humidity_percentage,
                        it.data.main.humidity.toString()
                    )
                    binding.temp.text = it.data.main.temp.toString()
                    binding.location.text = getString(
                        R.string.current_location,
                        it.data.name,
                        it.data.sys.country
                    )
                    binding.weather.text = it.data.weather.first().main
                    binding.description.text = it.data.weather.first().description
                    GlideApp.with(this)
                        .load(getWeatherIcon(it.data.weather.first().main))
                        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                        .into(binding.weatherIcon)
                }
                Status.ERROR -> {
                    progress.dismiss()
                    showErrorToasty(it.message.toString())
                }
                Status.LOADING -> {
                    progress.show()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationServices()
                } else {
                    showWarningToasty(getString(R.string.permission_denied))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GPS -> {
                if (resultCode == Activity.RESULT_OK) {
                    getDeviceLocation()
                }
            }
        }
    }

}