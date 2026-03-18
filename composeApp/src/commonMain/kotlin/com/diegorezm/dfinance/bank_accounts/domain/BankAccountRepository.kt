package com.diegorezm.dfinance.bank_accounts.domain

import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun getAccounts(): Flow<List<BankAccount>>
    suspend fun insertAccount(account: BankAccount)
    suspend fun updateAccount(account: BankAccount)
    suspend fun deleteAccount(id: Long)
}
