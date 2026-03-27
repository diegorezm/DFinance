package com.diegorezm.dfinance.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.core.domain.onError
import com.diegorezm.dfinance.core.domain.onSuccess
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.domain.SettingsKeys
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class TransactionsViewModel(
    private val transactionsRepository: TransactionRepository,
    private val bankRepository: BankAccountRepository,
    private val settings: Settings,
    val bankId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.onStart {
        fetchAccount()
        fetchAccounts()
        // Default to current month
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = "${now.year}-${now.month.number.toString().padStart(2, '0')}"
        
        val chartTypeString = settings.getString(SettingsKeys.KEY_CHART_TYPE, ChartType.BAR.name)
        val chartType = try {
            ChartType.valueOf(chartTypeString)
        } catch (e: Exception) {
            ChartType.BAR
        }

        _state.value = _state.value.copy(
            selectedMonth = currentMonth,
            currentChartType = chartType
        )
        fetchTransactions()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    private var fetchJob: Job? = null

    fun onAction(action: TransactionsActions) {
        when (action) {
            is TransactionsActions.OnFilterSelected -> {
                val currentFilters = state.value.activeFilters
                val filter = action.filter

                if (filter == null) {
                    _state.value = _state.value.copy(activeFilters = emptySet())
                    return
                }

                val updatedFilters = if (filter in currentFilters) {
                    currentFilters - filter
                } else {
                    currentFilters + filter
                }

                _state.value = _state.value.copy(activeFilters = updatedFilters)
            }

            TransactionsActions.OnAddTransactionClick -> {
                _state.value = _state.value.copy(isCreateSheetOpen = true)
            }

            TransactionsActions.OnDismissCreateSheet -> {
                _state.value = _state.value.copy(isCreateSheetOpen = false)
            }

            TransactionsActions.OnChartToggle -> {
                _state.value = _state.value.copy(isChartExpanded = !_state.value.isChartExpanded)
            }

            is TransactionsActions.OnConfirmCreateTransaction -> {
                viewModelScope.launch {
                    transactionsRepository.create(action.dto)
                    // fetchTransactions() // Refresh after create
                    _state.value = _state.value.copy(isCreateSheetOpen = false)
                }
            }

            is TransactionsActions.OnEditTransactionClick -> {
                _state.value = _state.value.copy(editingTransaction = action.transaction)
            }

            TransactionsActions.OnDismissEditSheet -> {
                _state.value = _state.value.copy(editingTransaction = null)
            }

            is TransactionsActions.OnConfirmEditTransaction -> {
                viewModelScope.launch {
                    val id = _state.value.editingTransaction?.id ?: return@launch
                    transactionsRepository.update(id, action.dto)
                    fetchTransactions() // Refresh after update
                    _state.value = _state.value.copy(editingTransaction = null)
                }
            }

            is TransactionsActions.OnDeleteTransactionClick -> {
                viewModelScope.launch {
                    transactionsRepository.delete(action.id)
                    fetchTransactions() // Refresh after delete
                }
            }

            is TransactionsActions.OnDateRangeSelected -> {
                _state.value = _state.value.copy(
                    startDate = action.startDate,
                    endDate = action.endDate,
                    selectedMonth = null
                )
                fetchTransactions()
            }

            TransactionsActions.OnClearDateFilter -> {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val currentMonth = "${now.year}-${now.monthNumber.toString().padStart(2, '0')}"
                _state.value = _state.value.copy(
                    selectedMonth = currentMonth,
                    startDate = null,
                    endDate = null
                )
                fetchTransactions()
            }

            else -> {}
        }

    }

    private fun fetchTransactions() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val currentState = _state.value
            val flow = when {
                currentState.startDate != null && currentState.endDate != null -> {
                    transactionsRepository.findByDateBetween(
                        bankId,
                        currentState.startDate.toString(),
                        currentState.endDate.toString()
                    )
                }

                currentState.selectedMonth != null -> {
                    transactionsRepository.findByMonth(bankId, currentState.selectedMonth)
                }

                else -> {
                    transactionsRepository.findByAccountId(bankId)
                }
            }

            flow.onStart {
                _state.value = _state.value.copy(isLoading = true)
            }
                .catch {
                    _state.value = _state.value.copy(error = it.message, isLoading = false)
                }
                .collect { transactions ->
                    _state.value = _state.value.copy(transactions = transactions, isLoading = false)
                }
        }
    }

    private fun fetchAccount() {
        viewModelScope.launch {
            bankRepository.findById(bankId).onSuccess {
                _state.value = _state.value.copy(account = it)
            }.onError {
                _state.value = _state.value.copy(error = it.name)
            }
        }
    }

    private fun fetchAccounts() {
        viewModelScope.launch {
            bankRepository.findAll()
                .catch {
                    _state.value = _state.value.copy(error = it.message)
                }
                .collect { accounts ->
                    _state.value = _state.value.copy(accounts = accounts)
                }
        }
    }

}
