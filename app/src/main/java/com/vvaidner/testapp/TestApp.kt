package com.vvaidner.testapp

import android.app.Application
import com.vvaidner.testapp.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(appComponent)
        }
    }
}