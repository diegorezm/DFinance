package com.diegorezm.dfinance.transactions.data.dto

import com.diegorezm.dfinance.transactions.domain.TransactionType

data class TransactionDTO(
    val accountId: Long,
    val subcategoryId: Long?,
    val toAccountId: Long?,
    val type: TransactionType,
    val amount: Long,
    val note: String?,
    val date: String
)
