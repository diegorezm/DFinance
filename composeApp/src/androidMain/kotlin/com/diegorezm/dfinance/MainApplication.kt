package com.diegorezm.dfinance

import android.app.Application
import com.diegorezm.dfinance.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApplication)
        }
    }
}
