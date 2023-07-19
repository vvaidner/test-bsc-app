package com.vvaidner.testapp.di

import com.vvaidner.testapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(
            todoListUseCase= get(),
        )
    }
}
