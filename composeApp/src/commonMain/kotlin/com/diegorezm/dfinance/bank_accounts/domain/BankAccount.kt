package com.diegorezm.dfinance.bank_accounts.domain

data class BankAccount(
    val id: Long,
    val name: String,
    val currencyCode: String,
    val color: String
)
