package com.diegorezm.dfinance.di

import com.diegorezm.dfinance.db.DatabaseDriverFactory
import com.diegorezm.dfinance.settings.domain.SettingsFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
    single { SettingsFactory() }
}