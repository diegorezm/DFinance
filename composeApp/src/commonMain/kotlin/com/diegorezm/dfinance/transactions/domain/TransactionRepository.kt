package com.diegorezm.dfinance.transactions.domain

import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun findAll(): Flow<List<Transaction>>
    fun findByAccountId(accountId: Long): Flow<List<Transaction>>
    fun findByMonth(accountId: Long, month: String): Flow<List<Transaction>>
    fun findByDateBetween(
        accountId: Long,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>>

    suspend fun create(dto: TransactionDTO): EmptyResult<DataError.Local>
    suspend fun update(id: Long, dto: TransactionDTO): EmptyResult<DataError.Local>
    suspend fun delete(id: Long): EmptyResult<DataError.Local>
}
