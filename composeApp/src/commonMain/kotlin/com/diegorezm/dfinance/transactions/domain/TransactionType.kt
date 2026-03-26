package com.diegorezm.dfinance.transactions.domain

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
    INITIAL_BALANCE;

    companion object {
        fun fromString(value: String): TransactionType =
            entries.find { it.name == value } ?: EXPENSE
    }
}
