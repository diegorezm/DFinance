package com.diegorezm.dfinance.transactions.presentation

import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType

sealed interface TransactionsActions {
    object OnAddTransactionClick : TransactionsActions
    object OnBackClick : TransactionsActions
    data class OnFilterSelected(val filter: TransactionType?) : TransactionsActions
    data class OnDeleteTransactionClick(val id: Long) : TransactionsActions
    object OnDismissCreateSheet : TransactionsActions
    data class OnConfirmCreateTransaction(val dto: TransactionDTO) : TransactionsActions
    object OnDismissEditSheet : TransactionsActions
    data class OnConfirmEditTransaction(val dto: TransactionDTO) : TransactionsActions
    data class OnEditTransactionClick(val transaction: Transaction) : TransactionsActions
}
