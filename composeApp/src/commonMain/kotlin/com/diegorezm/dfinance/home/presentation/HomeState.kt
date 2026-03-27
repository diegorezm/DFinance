package com.diegorezm.dfinance.home.presentation

import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.transactions.domain.Transaction

data class HomeState(
    val totalNetWorth: Long = 0L,
    val accountSummaries: List<AccountSummary> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val budgetGoals: List<BudgetGoal> = emptyList(),
    val isChartExpanded: Boolean = true,
    val chartType: ChartType = ChartType.BAR,
    val isLoading: Boolean = false
)

data class AccountSummary(
    val account: BankAccount,
    val balance: Long
)

data class BudgetGoal(
    val label: String,
    val spent: Long,
    val target: Long,
    val color: String // Hex color
)
