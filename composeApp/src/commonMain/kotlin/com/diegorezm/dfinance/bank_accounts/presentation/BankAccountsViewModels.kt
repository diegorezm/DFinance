package com.diegorezm.dfinance.bank_accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BankAccountsViewModel(
    private val repository: BankAccountRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BankAccountState())
    val state: StateFlow<BankAccountState> = _state.onStart {
        fetchAccounts()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun fetchAccounts() {
        viewModelScope.launch {
            repository.findAll()
                .onStart {
                    _state.value = _state.value.copy(isLoading = true)
                }
                .map { accounts ->
                    _state.value = _state.value.copy(isLoading = false, accounts = accounts)
                }.catch {
                    _state.value = _state.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun createAccount(account: BankAccount) {
        viewModelScope.launch {
            repository.create(account)
        }
    }

    fun updateAccount(account: BankAccount) {
        viewModelScope.launch {
            repository.update(account)
        }
    }

    fun deleteAccount(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }
}
