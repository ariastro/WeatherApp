package com.astronout.weatherapp.di.module

import com.astronout.weatherapp.data.remote.repository.RemoteRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        RemoteRepository(get())
    }
}