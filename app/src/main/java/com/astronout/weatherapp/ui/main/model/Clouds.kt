package com.astronout.weatherapp.ui.main.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all")
    val all: Int
)