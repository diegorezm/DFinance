package com.diegorezm.dfinance.settings.presentation

data class SettingsState(
    val needPercentage: Int = 50,
    val wantPercentage: Int = 30,
    val savingPercentage: Int = 20,
    val totalPercentage: Int = 100
)
