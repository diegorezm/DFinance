package com.diegorezm.dfinance.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.domain.SettingsKeys
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import com.diegorezm.dfinance.transactions.domain.TransactionType
import com.russhwolf.settings.Settings
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.bucket_needs
import dfinance.composeapp.generated.resources.bucket_savings
import dfinance.composeapp.generated.resources.bucket_wants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock

class HomeViewModel(
    bankAccountRepository: BankAccountRepository,
    transactionRepository: TransactionRepository,
    private val settings: Settings
) : ViewModel() {

    private val _isChartExpanded = MutableStateFlow(true)

    val state = combine(
        bankAccountRepository.findAll(),
        transactionRepository.findAll(),
        _isChartExpanded
    ) { accounts, transactions, isChartExpanded ->
        val accountSummaries = buildAccountSummaries(accounts, transactions)
        val totalNetWorth = calculateNetWorth(accountSummaries)
        val currentMonthTransactions = filterCurrentMonthTransactions(transactions)
        val totalIncome = calculateTotalIncome(currentMonthTransactions)
        val budgetGoals = buildBudgetGoals(currentMonthTransactions, totalIncome)
        val chartType = resolveChartType()

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
            HomeActions.OnToggleChart ->
                _isChartExpanded.value = !_isChartExpanded.value

            else -> {}
        }
    }

    // --- Private helpers ---

    private fun buildAccountSummaries(
        accounts: List<BankAccount>,
        transactions: List<Transaction>
    ): List<AccountSummary> {
        return accounts.map { account ->
            val balance = calculateAccountBalance(account, transactions)
            AccountSummary(account, balance)
        }
    }

    private fun calculateAccountBalance(
        account: BankAccount,
        transactions: List<Transaction>
    ): Long {
        return transactions
            .filter { it.accountId == account.id || it.toAccountId == account.id }
            .sumOf { tx -> resolveTransactionAmount(tx, account.id) }
    }

    private fun resolveTransactionAmount(tx: Transaction, accountId: Long): Long {
        return when (tx.type) {
            TransactionType.INCOME ->
                if (tx.accountId == accountId) tx.amount else 0L

            TransactionType.EXPENSE ->
                if (tx.accountId == accountId) {
                    // Savings are considered positive for net worth calculation
                    if (tx.budgetBucket == BudgetBucket.SAVING) tx.amount else -tx.amount
                } else 0L

            TransactionType.TRANSFER -> when {
                tx.accountId == accountId -> -tx.amount
                tx.toAccountId == accountId -> tx.amount
                else -> 0L
            }
        }
    }

    private fun calculateNetWorth(summaries: List<AccountSummary>): Long {
        return summaries.sumOf { it.balance }
    }

    private fun filterCurrentMonthTransactions(transactions: List<Transaction>): List<Transaction> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = "${now.year}-${now.month.number.toString().padStart(2, '0')}"
        return transactions.filter { it.date.startsWith(currentMonth) }
    }

    private fun calculateTotalIncome(transactions: List<Transaction>): Long {
        return transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }

    private fun buildBudgetGoals(
        transactions: List<Transaction>,
        totalIncome: Long
    ): List<BudgetGoal> {
        val needPct = settings.getInt(SettingsKeys.KEY_NEED_PERCENTAGE, 50)
        val wantPct = settings.getInt(SettingsKeys.KEY_WANT_PERCENTAGE, 30)
        val savingPct = settings.getInt(SettingsKeys.KEY_SAVING_PERCENTAGE, 20)

        return listOf(
            buildBudgetGoal(
                label = Res.string.bucket_needs,
                bucket = BudgetBucket.NEED,
                transactions = transactions,
                totalIncome = totalIncome,
                percentage = needPct,
                color = "#4CAF50"
            ),
            buildBudgetGoal(
                label = Res.string.bucket_wants,
                bucket = BudgetBucket.WANT,
                transactions = transactions,
                totalIncome = totalIncome,
                percentage = wantPct,
                color = "#2196F3"
            ),
            buildBudgetGoal(
                label = Res.string.bucket_savings,
                bucket = BudgetBucket.SAVING,
                transactions = transactions,
                totalIncome = totalIncome,
                percentage = savingPct,
                color = "#FFC107"
            )
        )
    }

    private fun buildBudgetGoal(
        label: StringResource,
        bucket: BudgetBucket,
        transactions: List<Transaction>,
        totalIncome: Long,
        percentage: Int,
        color: String
    ): BudgetGoal {
        val spent = transactions
            .filter { it.type == TransactionType.EXPENSE && it.budgetBucket == bucket }
            .sumOf { it.amount }

        return BudgetGoal(
            label = label,
            spent = spent,
            target = (totalIncome * percentage) / 100,
            color = color
        )
    }

    private fun resolveChartType(): ChartType {
        val raw = settings.getString(SettingsKeys.KEY_CHART_TYPE, ChartType.BAR.name)
        return try {
            ChartType.valueOf(raw)
        } catch (e: Exception) {
            ChartType.BAR
        }
    }
}

