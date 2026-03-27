package com.diegorezm.dfinance.transactions.presentation

import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType
import kotlinx.datetime.LocalDate

data class TransactionsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val account: BankAccount? = null,
    val accounts: List<BankAccount> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val activeFilters: Set<TransactionType> = emptySet(),
    val isCreateSheetOpen: Boolean = false,
    val editingTransaction: Transaction? = null,
    val isChartExpanded: Boolean = true,
    val selectedMonth: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
) {
    val filteredTransactions: List<Transaction>
        get() = if (activeFilters.isEmpty()) transactions
        else transactions.filter { activeFilters.contains(it.type) }

    val totalIncome: Long
        get() = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

    val totalExpenses: Long
        get() = transactions
            .filter { it.type == TransactionType.EXPENSE || it.type == TransactionType.TRANSFER }
            .sumOf { it.amount }

    val isDateFiltered: Boolean
        get() = selectedMonth != null || (startDate != null && endDate != null)
}
