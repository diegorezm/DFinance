package com.diegorezm.dfinance.bank_accounts.data.dto

data class BankAccountDTO(
    val name: String,
    val currencyCode: String,
    val balance: Long
)