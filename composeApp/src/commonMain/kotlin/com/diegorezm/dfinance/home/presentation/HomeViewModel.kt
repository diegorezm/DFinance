package com.diegorezm.dfinance.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.domain.SettingsKeys
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import com.diegorezm.dfinance.transactions.domain.TransactionType
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class HomeViewModel(
    private val bankAccountRepository: BankAccountRepository,
    private val transactionRepository: TransactionRepository,
    private val settings: Settings
) : ViewModel() {

    private val _isChartExpanded = MutableStateFlow(true)

    val state = combine(
        bankAccountRepository.findAll(),
        transactionRepository.findAll(),
        _isChartExpanded
    ) { accounts, transactions, isChartExpanded ->
        val accountSummaries = accounts.map { account ->
            val accountTransactions =
                transactions.filter { it.accountId == account.id || it.toAccountId == account.id }
            val balance = accountTransactions.sumOf { tx ->
                when (tx.type) {
                    TransactionType.INCOME -> if (tx.accountId == account.id) tx.amount else 0L
                    TransactionType.EXPENSE -> {
                        if (tx.accountId == account.id) {
                            // Savings are considered positive for net worth/balance calculation
                            if (tx.budgetBucket == BudgetBucket.SAVING) tx.amount else -tx.amount
                        } else 0L
                    }

                    TransactionType.TRANSFER -> {
                        when {
                            tx.accountId == account.id -> -tx.amount
                            tx.toAccountId == account.id -> tx.amount
                            else -> 0L
                        }
                    }
                }
            }
            AccountSummary(account, balance)
        }

        val totalNetWorth = accountSummaries.sumOf { it.balance }

        val chartTypeString = settings.getString(SettingsKeys.KEY_CHART_TYPE, ChartType.BAR.name)
        val chartType = try {
            ChartType.valueOf(chartTypeString)
        } catch (e: Exception) {
            ChartType.BAR
        }

        // Budget Goals calculation
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = "${now.year}-${now.month.number.toString().padStart(2, '0')}"

        val currentMonthTransactions = transactions.filter { it.date.startsWith(currentMonth) }

        val totalIncome = currentMonthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val needPercentage = settings.getInt(SettingsKeys.KEY_NEED_PERCENTAGE, 50)
        val wantPercentage = settings.getInt(SettingsKeys.KEY_WANT_PERCENTAGE, 30)
        val savingPercentage = settings.getInt(SettingsKeys.KEY_SAVING_PERCENTAGE, 20)

        val budgetGoals = listOf(
            BudgetGoal(
                label = "Needs",
                spent = currentMonthTransactions
                    .filter { it.type == TransactionType.EXPENSE && it.budgetBucket == BudgetBucket.NEED }
                    .sumOf { it.amount },
                target = (totalIncome * needPercentage) / 100,
                color = "#4CAF50"
            ),
            BudgetGoal(
                label = "Wants",
                spent = currentMonthTransactions
                    .filter { it.type == TransactionType.EXPENSE && it.budgetBucket == BudgetBucket.WANT }
                    .sumOf { it.amount },
                target = (totalIncome * wantPercentage) / 100,
                color = "#2196F3"
            ),
            BudgetGoal(
                label = "Savings",
                spent = currentMonthTransactions
                    .filter { it.type == TransactionType.EXPENSE && it.budgetBucket == BudgetBucket.SAVING }
                    .sumOf { it.amount },
                target = (totalIncome * savingPercentage) / 100,
                color = "#FFC107"
            )
        )

        HomeState(
            totalNetWorth = totalNetWorth,
            accountSummaries = accountSummaries,
            transactions = transactions,
            budgetGoals = budgetGoals,
            isChartExpanded = isChartExpanded,
            chartType = chartType,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeState(isLoading = true)
    )

    fun onAction(action: HomeActions) {
        when (action) {
            HomeActions.OnToggleChart -> {
                _isChartExpanded.value = !_isChartExpanded.value
            }

            else -> {}
        }
    }
}
