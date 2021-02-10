package com.astronout.weatherapp.di.module

import android.content.Context
import com.astronout.weatherapp.data.local.AppDatabase
import com.astronout.weatherapp.data.local.repository.LocalRepository
import com.astronout.weatherapp.data.remote.repository.RemoteRepository
import com.astronout.weatherapp.utils.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single { RemoteRepository(get()) }
    single { provideAppDatabase(androidContext()) }
    single { LocalRepository(get()) }
}

private fun provideAppDatabase(context: Context) = AppDatabase.getDatabase(context)