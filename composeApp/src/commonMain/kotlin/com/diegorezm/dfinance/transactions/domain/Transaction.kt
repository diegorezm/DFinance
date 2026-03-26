package com.diegorezm.dfinance.transactions.domain

data class Transaction(
    val id: Long,
    val accountId: Long,
    val subcategoryId: Long?,
    val toAccountId: Long?,
    val type: TransactionType,
    val amount: Long,
    val note: String?,
    val date: String
)
