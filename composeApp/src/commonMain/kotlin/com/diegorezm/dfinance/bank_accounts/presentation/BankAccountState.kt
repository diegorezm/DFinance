package com.diegorezm.dfinance.bank_accounts.presentation

import com.diegorezm.dfinance.bank_accounts.domain.BankAccount

data class BankAccountState(
    val isLoading: Boolean = false,
    val accounts: List<BankAccount> = emptyList(),
    val error: String? = null,
    val isCreateSheetOpen: Boolean = false,
    val editingAccount: BankAccount? = null
)
