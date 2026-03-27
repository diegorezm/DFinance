package com.diegorezm.dfinance.settings.presentation

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settings: Settings
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val need = settings.getInt(KEY_NEED_PERCENTAGE, 50)
        val want = settings.getInt(KEY_WANT_PERCENTAGE, 30)
        val saving = settings.getInt(KEY_SAVING_PERCENTAGE, 20)

        _state.update {
            it.copy(
                needPercentage = need,
                wantPercentage = want,
                savingPercentage = saving,
                totalPercentage = need + want + saving
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
            SettingsActions.OnSaveClick -> saveSettings()
        }
    }

    private fun saveSettings() {
        val currentState = _state.value
        settings[KEY_NEED_PERCENTAGE] = currentState.needPercentage
        settings[KEY_WANT_PERCENTAGE] = currentState.wantPercentage
        settings[KEY_SAVING_PERCENTAGE] = currentState.savingPercentage
    }

    companion object {
        const val KEY_NEED_PERCENTAGE = "need_percentage"
        const val KEY_WANT_PERCENTAGE = "want_percentage"
        const val KEY_SAVING_PERCENTAGE = "saving_percentage"
    }
}
