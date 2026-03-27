package com.diegorezm.dfinance.settings.presentation

sealed interface SettingsActions {
    data class OnNeedPercentageChange(val percentage: Int) : SettingsActions
    data class OnWantPercentageChange(val percentage: Int) : SettingsActions
    data class OnSavingPercentageChange(val percentage: Int) : SettingsActions
    object OnSaveClick : SettingsActions
}
