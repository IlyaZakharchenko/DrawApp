package com.example.drawapp.koin

import com.example.drawapp.ImageSaver
import com.example.drawapp.ViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ViewModel(get())
    }

    single {
        ImageSaver(get())
    }

    single {
        androidApplication().contentResolver
    }
}