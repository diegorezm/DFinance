package com.diegorezm.dfinance.bank_accounts.domain

import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun findAll(): Flow<List<BankAccount>>
    suspend fun create(account: BankAccount): EmptyResult<DataError.Local>
    suspend fun update(account: BankAccount): EmptyResult<DataError.Local>
    suspend fun delete(id: Long): EmptyResult<DataError.Local>
}
