package com.diegorezm.dfinance

import android.app.Application
import com.diegorezm.dfinance.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class DFinanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@DFinanceApp)
        }
    }
}
