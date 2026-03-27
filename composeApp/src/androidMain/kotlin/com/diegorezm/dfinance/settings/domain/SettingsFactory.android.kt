package com.diegorezm.dfinance.settings.domain

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class SettingsFactory(
    private val context: Context
) {
    actual fun createSettings(): Settings {
        val sharedPrefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(sharedPrefs)
    }
}