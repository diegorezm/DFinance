package com.diegorezm.dfinance.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.Currency
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.domain.SettingsKeys
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settings: Settings
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val need = settings.getInt(SettingsKeys.KEY_NEED_PERCENTAGE, 50)
        val want = settings.getInt(SettingsKeys.KEY_WANT_PERCENTAGE, 30)
        val saving = settings.getInt(SettingsKeys.KEY_SAVING_PERCENTAGE, 20)
        val chartTypeString = settings.getString(SettingsKeys.KEY_CHART_TYPE, ChartType.BAR.name)
        val chartType = try {
            ChartType.valueOf(chartTypeString)
        } catch (e: Exception) {
            ChartType.BAR
        }
        val currencyCode = settings.getString(SettingsKeys.KEY_CURRENCY, Currency.BRL.code)
        val currency = Currency.fromCode(currencyCode)

        _state.update {
            it.copy(
                needPercentage = need,
                wantPercentage = want,
                savingPercentage = saving,
                totalPercentage = need + want + saving,
                chartType = chartType,
                currency = currency
            )
        }
    }

    fun onAction(action: SettingsActions) {
        when (action) {
            is SettingsActions.OnNeedPercentageChange -> {
                _state.update {
                    it.copy(
                        needPercentage = action.percentage,
                        totalPercentage = action.percentage + it.wantPercentage + it.savingPercentage
                    )
                }
            }
            is SettingsActions.OnWantPercentageChange -> {
                _state.update {
                    it.copy(
                        wantPercentage = action.percentage,
                        totalPercentage = it.needPercentage + action.percentage + it.savingPercentage
                    )
                }
            }
            is SettingsActions.OnSavingPercentageChange -> {
                _state.update {
                    it.copy(
                        savingPercentage = action.percentage,
                        totalPercentage = it.needPercentage + it.wantPercentage + action.percentage
                    )
                }
            }
            is SettingsActions.OnChartTypeChange -> {
                _state.update {
                    it.copy(chartType = action.chartType)
                }
            }
            is SettingsActions.OnCurrencyChange -> {
                _state.update {
                    it.copy(currency = action.currency)
                }
            }
            SettingsActions.OnSaveClick -> saveSettings()
            SettingsActions.OnDismissSuccessMessage -> {
                _state.update { it.copy(showSaveSuccess = false) }
            }
        }
    }

    private fun saveSettings() {
        val currentState = _state.value
        settings[SettingsKeys.KEY_NEED_PERCENTAGE] = currentState.needPercentage
        settings[SettingsKeys.KEY_WANT_PERCENTAGE] = currentState.wantPercentage
        settings[SettingsKeys.KEY_SAVING_PERCENTAGE] = currentState.savingPercentage
        settings[SettingsKeys.KEY_CHART_TYPE] = currentState.chartType.name
        settings[SettingsKeys.KEY_CURRENCY] = currentState.currency.code
        
        _state.update { it.copy(showSaveSuccess = true) }
        
        viewModelScope.launch {
            delay(3000)
            _state.update { it.copy(showSaveSuccess = false) }
        }
    }
}
