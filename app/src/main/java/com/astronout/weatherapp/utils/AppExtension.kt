package com.astronout.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.astronout.weatherapp.R
import es.dmoral.toasty.Toasty


fun visible() = View.VISIBLE
fun invisible() = View.INVISIBLE
fun gone() = View.GONE

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showSuccessToasty(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showErrorToasty(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showInfoToasty(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showWarningToasty(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.checkLocationSetting(): Boolean {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Activity.hasPermissionLocation(context: Context, REQUEST_PERMISSION_CODE: Int): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            // Show the permission request
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSION_CODE
            )
            false
        }
    } else {
        true
    }
}

fun getWeatherIcon(weather: String?): Int {
    var weatherIcon: Int? = null
    when (weather) {
        "Thunderstorm" -> weatherIcon = R.drawable.weather_thunderstorm
        "Drizzle" -> weatherIcon = R.drawable.weather_rainy
        "Rain" -> weatherIcon = R.drawable.weather_rainy
        "Snow" -> weatherIcon = R.drawable.weather_snow
        "Atmosphere" -> weatherIcon = R.drawable.weather_clear
        "Clear" -> weatherIcon = R.drawable.weather_clear
        "Clouds" -> weatherIcon = R.drawable.weather_cloud
        "Haze" -> weatherIcon = R.drawable.weather_cloud
        "Extreme" -> weatherIcon = R.drawable.weather_thunderstorm
    }
    return weatherIcon!!
}