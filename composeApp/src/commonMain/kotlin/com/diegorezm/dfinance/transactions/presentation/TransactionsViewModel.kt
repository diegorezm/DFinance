package com.diegorezm.dfinance.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val repository: TransactionRepository,
    val bankId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.onStart {
        fetchTransactions()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

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

            else -> {}
        }

    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            repository.findByAccountId(bankId)
                .onStart {
                    _state.value = _state.value.copy(isLoading = true)
                }
                .catch {
                    _state.value = _state.value.copy(error = it.message, isLoading = false)
                }
                .collect { transactions ->
                    _state.value = _state.value.copy(transactions = transactions, isLoading = false)
                }

            _state.value = _state.value.copy(isLoading = false)
        }
    }

}