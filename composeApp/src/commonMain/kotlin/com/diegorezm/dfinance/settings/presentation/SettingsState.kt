package com.diegorezm.dfinance.settings.presentation

import com.diegorezm.dfinance.bank_accounts.domain.Currency
import com.diegorezm.dfinance.settings.domain.ChartType

data class SettingsState(
    val needPercentage: Int = 50,
    val wantPercentage: Int = 30,
    val savingPercentage: Int = 20,
    val totalPercentage: Int = 100,
    val chartType: ChartType = ChartType.BAR,
    val currency: Currency = Currency.BRL,
    val showSaveSuccess: Boolean = false
)
