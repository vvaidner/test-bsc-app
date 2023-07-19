package com.vvaidner.testapp.di

import com.vvaidner.domain.usecase.TodoListUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single {
        TodoListUseCase(
            coroutineContext = get(),
            repository = get(),
        )
    }
}
