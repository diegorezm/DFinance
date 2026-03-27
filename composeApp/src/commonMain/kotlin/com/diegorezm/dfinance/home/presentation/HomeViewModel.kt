package com.diegorezm.dfinance.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import com.diegorezm.dfinance.transactions.domain.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val bankAccountRepository: BankAccountRepository,
    private val transactionRepository: TransactionRepository
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
                    TransactionType.EXPENSE -> if (tx.accountId == account.id) -tx.amount else 0L
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

        HomeState(
            totalNetWorth = totalNetWorth,
            accountSummaries = accountSummaries,
            transactions = transactions,
            isChartExpanded = isChartExpanded,
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
