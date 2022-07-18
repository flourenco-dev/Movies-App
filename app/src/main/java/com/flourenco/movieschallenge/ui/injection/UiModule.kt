package com.flourenco.movieschallenge.ui.injection

import com.flourenco.movieschallenge.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        MainViewModel(
            repository = get(),
            networkManager = get()
        )
    }
}