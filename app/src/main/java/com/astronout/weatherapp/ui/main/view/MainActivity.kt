package com.astronout.weatherapp.ui.main.view

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astronout.weatherapp.R
import com.astronout.weatherapp.base.BaseActivity
import com.astronout.weatherapp.databinding.ActivityMainBinding
import com.astronout.weatherapp.ui.main.viewmodel.MainViewModel
import com.astronout.weatherapp.utils.getWeatherIcon
import com.astronout.weatherapp.utils.glide.GlideApp
import com.astronout.weatherapp.utils.showErrorToasty
import com.astronout.weatherapp.vo.Status
import com.bumptech.glide.GenericTransitionOptions
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupDayNight()
        viewModel.getWeather("Malang")
        observeWeather()

    }

    private fun setupDayNight() {
        when (DateTime().hourOfDay().get()) {
            in 0..17 -> {
                binding.parent.background = ContextCompat.getDrawable(this, R.drawable.morning)
                window.statusBarColor = ContextCompat.getColor(this, R.color.dayStatusBarColor)
            }
            in 18..24 -> {
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
                    binding.cloud.text = getString(R.string.cloud_percentage, it.data!!.clouds.all.toString())
                    binding.windSpeed.text = getString(R.string.wind_speed, it.data.wind.speed.toString())
                    binding.humidity.text = getString(R.string.humidity_percentage, it.data.main.humidity.toString())
                    binding.temp.text = it.data.main.temp.toString()
                    binding.location.text = getString(R.string.current_location, it.data.name, it.data.sys.country)
                    binding.weather.text = it.data.weather.first().main
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

}