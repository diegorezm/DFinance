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
                initialNeedPercentage = need,
                initialWantPercentage = want,
                initialSavingPercentage = saving,
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
                    )
                }
            }

            is SettingsActions.OnWantPercentageChange -> {
                _state.update {
                    it.copy(
                        wantPercentage = action.percentage,
                    )
                }
            }

            is SettingsActions.OnSavingPercentageChange -> {
                _state.update {
                    it.copy(
                        savingPercentage = action.percentage,
                    )
                }
            }

            is SettingsActions.OnChartTypeChange -> {
                _state.update {
                    it.copy(chartType = action.chartType)
                }
                settings[SettingsKeys.KEY_CHART_TYPE] = action.chartType.name
            }

            is SettingsActions.OnCurrencyChange -> {
                _state.update {
                    it.copy(currency = action.currency)
                }
                settings[SettingsKeys.KEY_CURRENCY] = action.currency.code
            }

            SettingsActions.OnSaveClick -> saveBudgetTargets()
            SettingsActions.OnDismissSuccessMessage -> {
                _state.update { it.copy(showSaveSuccess = false) }
            }
        }
    }

    private fun saveBudgetTargets() {
        val currentState = _state.value
        settings[SettingsKeys.KEY_NEED_PERCENTAGE] = currentState.needPercentage
        settings[SettingsKeys.KEY_WANT_PERCENTAGE] = currentState.wantPercentage
        settings[SettingsKeys.KEY_SAVING_PERCENTAGE] = currentState.savingPercentage

        _state.update {
            it.copy(
                initialNeedPercentage = it.needPercentage,
                initialWantPercentage = it.wantPercentage,
                initialSavingPercentage = it.savingPercentage,
                showSaveSuccess = true
            )
        }

        viewModelScope.launch {
            delay(3000)
            _state.update { it.copy(showSaveSuccess = false) }
        }
    }
}
