package com.diegorezm.dfinance.bank_accounts.domain

data class BankAccount(
    val id: Long? = null,
    val name: String,
    val currencyCode: String,
    val balance: Long,
    val icon: String,
    val color: String
)
