package com.diegorezm.dfinance.settings.presentation

import com.diegorezm.dfinance.settings.domain.ChartType

sealed interface SettingsActions {
    data class OnNeedPercentageChange(val percentage: Int) : SettingsActions
    data class OnWantPercentageChange(val percentage: Int) : SettingsActions
    data class OnSavingPercentageChange(val percentage: Int) : SettingsActions
    data class OnChartTypeChange(val chartType: ChartType) : SettingsActions
    object OnSaveClick : SettingsActions
    object OnDismissSuccessMessage : SettingsActions
}
