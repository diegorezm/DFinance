package com.diegorezm.dfinance.transactions.presentation

import com.diegorezm.dfinance.transactions.domain.TransactionType

sealed interface TransactionsActions {
    object OnAddTransactionClick : TransactionsActions
    object OnBackClick : TransactionsActions
    data class OnFilterSelected(val filter: TransactionType?) : TransactionsActions
    data class OnDeleteTransactionClick(val id: Long) : TransactionsActions
}
