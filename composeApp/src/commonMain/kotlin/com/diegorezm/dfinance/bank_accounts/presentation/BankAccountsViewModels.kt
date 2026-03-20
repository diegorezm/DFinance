package com.diegorezm.dfinance.bank_accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.data.dto.BankAccountDTO
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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

            is BankAccountsActions.OnAccountClick -> { /* navigate to detail */
            }

            is BankAccountsActions.OnEditAccountClick -> { /* navigate to edit */
            }

            is BankAccountsActions.OnDeleteAccountClick -> deleteAccount(action.id)

            is BankAccountsActions.OnDismissCreateSheet -> {
                _state.update { it.copy(isCreateSheetOpen = false) }
            }

            is BankAccountsActions.OnConfirmCreateAccount -> {
                createAccount(action.dto)
                _state.update { it.copy(isCreateSheetOpen = false) }
            }
        }
    }

    fun fetchAccounts() {
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
            val account = BankAccount(
                name = dto.name,
                currencyCode = dto.currencyCode,
                balance = dto.balance,
                icon = "Default", // or get from dto if added
                color = dto.color
            )
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
