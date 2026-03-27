package com.diegorezm.dfinance.bank_accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.data.dto.BankAccountDTO
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.core.domain.onError
import com.diegorezm.dfinance.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankAccountsViewModel(
    private val repository: BankAccountRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BankAccountState())
    val state: StateFlow<BankAccountState> = _state.onStart {
        fetchAccounts()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun onAction(action: BankAccountsActions) {
        when (action) {
            is BankAccountsActions.OnAddAccountClick -> {
                _state.update { it.copy(isCreateSheetOpen = true) }
            }

            is BankAccountsActions.OnEditAccountClick -> {
                _state.update { it.copy(editingAccount = action.account) }
            }

            is BankAccountsActions.OnDeleteAccountClick -> deleteAccount(action.id)

            is BankAccountsActions.OnDismissCreateSheet -> {
                _state.update { it.copy(isCreateSheetOpen = false) }
            }

            is BankAccountsActions.OnConfirmCreateAccount -> {
                createAccount(action.dto)
                _state.update { it.copy(isCreateSheetOpen = false) }
            }

            is BankAccountsActions.OnDismissEditSheet -> {
                _state.update { it.copy(editingAccount = null) }
            }

            is BankAccountsActions.OnConfirmEditAccount -> {
                val currentAccount = _state.value.editingAccount
                if (currentAccount?.id != null) {
                    val updatedAccount = currentAccount.copy(
                        name = action.dto.name,
                        currencyCode = action.dto.currencyCode,
                        color = action.dto.color
                    )
                    updateAccount(updatedAccount)
                }
                _state.update { it.copy(editingAccount = null) }
            }

            else -> {}
        }
    }

    private fun fetchAccounts() {
        viewModelScope.launch {
            repository.findAll()
                .onStart {
                    _state.update { it.copy(isLoading = true) }
                }
                .catch { err ->
                    _state.update { it.copy(isLoading = false, error = err.message) }
                }
                .collect { accounts ->
                    _state.update { it.copy(isLoading = false, accounts = accounts) }
                }
        }
    }

    private fun createAccount(dto: BankAccountDTO) {
        viewModelScope.launch {
            repository.create(dto)
                .onSuccess {
                    _state.update { it.copy(error = null) }
                }
                .onError {
                    _state.update { it.copy(error = it.error.toString()) }
                }
        }
    }

    private fun updateAccount(account: BankAccount) {
        viewModelScope.launch {
            repository.update(account)
                .onSuccess {
                    _state.update { it.copy(error = null) }
                }
                .onError {
                    _state.update { it.copy(error = it.error.toString()) }
                }
        }
    }

    private fun deleteAccount(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    _state.update { it.copy(error = null) }
                }
                .onError {
                    _state.update { it.copy(error = it.error.toString()) }
                }
        }
    }
}
