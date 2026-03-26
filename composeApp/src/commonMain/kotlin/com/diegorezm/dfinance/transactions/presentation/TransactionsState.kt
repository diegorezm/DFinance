package com.diegorezm.dfinance.transactions.presentation

import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType

data class TransactionsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val account: BankAccount? = null,
    val transactions: List<Transaction> = emptyList(),
    val activeFilters: Set<TransactionType> = emptySet()
) {
    val filteredTransactions: List<Transaction>
        get() = if (activeFilters.isEmpty()) transactions
        else transactions.filter { activeFilters.contains(it.type) }

    val totalIncome: Long
        get() = transactions
            .filter { it.type == TransactionType.INCOME || it.type == TransactionType.INITIAL_BALANCE }
            .sumOf { it.amount }

    val totalExpenses: Long
        get() = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
}