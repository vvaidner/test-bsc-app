package com.vvaidner.testapp.di

import com.vvaidner.data.remote.TodosApi
import com.vvaidner.data.remote.TodosConfig
import com.vvaidner.data.repository.TodoListRepositoryImpl
import com.vvaidner.domain.repository.TodoListRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }


    single {
        GsonConverterFactory.create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(TodosConfig.BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get())
            .build()
    }

    single<TodosApi> {
        get<Retrofit>().create(TodosApi::class.java)
    }

    single<TodoListRepository> {
        TodoListRepositoryImpl(
            api = get(),
        )
    }
}