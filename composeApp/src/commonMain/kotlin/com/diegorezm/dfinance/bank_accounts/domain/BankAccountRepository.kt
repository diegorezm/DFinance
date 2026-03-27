package com.diegorezm.dfinance.bank_accounts.domain

import com.diegorezm.dfinance.bank_accounts.data.dto.BankAccountDTO
import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun findAll(): Flow<List<BankAccount>>
    fun findById(id: Long): Result<BankAccount, DataError.Local>
    suspend fun create(account: BankAccountDTO): EmptyResult<DataError.Local>
    suspend fun update(account: BankAccount): EmptyResult<DataError.Local>
    suspend fun delete(id: Long): EmptyResult<DataError.Local>
}
