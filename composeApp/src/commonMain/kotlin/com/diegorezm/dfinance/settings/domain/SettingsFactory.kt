package com.diegorezm.dfinance.settings.domain

import com.russhwolf.settings.Settings

expect class SettingsFactory {
    fun createSettings(): Settings
}