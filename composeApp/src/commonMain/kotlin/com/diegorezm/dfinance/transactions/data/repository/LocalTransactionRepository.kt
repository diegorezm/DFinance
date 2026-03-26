package com.diegorezm.dfinance.transactions.data.repository

import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.core.domain.Result
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LocalTransactionRepository : TransactionRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private var nextId = 1L

    override fun findAll(): Flow<List<Transaction>> {
        return _transactions.map { it.sortedByDescending { t -> t.date } }
    }

    override fun findByAccountId(accountId: Long): Flow<List<Transaction>> {
        return _transactions.map { list ->
            list.filter { it.accountId == accountId }
                .sortedByDescending { it.date }
        }
    }

    override fun findByMonth(month: String): Flow<List<Transaction>> {
        return _transactions.map { list ->
            list.filter { it.date.startsWith(month) }
                .sortedByDescending { it.date }
        }
    }

    override suspend fun create(dto: TransactionDTO): EmptyResult<DataError.Local> {
        val transaction = Transaction(
            id = nextId++,
            accountId = dto.accountId,
            subcategoryId = dto.subcategoryId,
            toAccountId = dto.toAccountId,
            type = dto.type,
            amount = dto.amount,
            note = dto.note,
            date = dto.date
        )
        _transactions.update { it + transaction }
        return Result.Success(Unit)
    }

    override suspend fun update(
        id: Long,
        dto: TransactionDTO
    ): EmptyResult<DataError.Local> {
        var found = false
        _transactions.update { list ->
            list.map {
                if (it.id == id) {
                    found = true
                    it.copy(
                        accountId = dto.accountId,
                        subcategoryId = dto.subcategoryId,
                        toAccountId = dto.toAccountId,
                        type = dto.type,
                        amount = dto.amount,
                        note = dto.note,
                        date = dto.date
                    )
                } else it
            }
        }
        return if (found) Result.Success(Unit) else Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun delete(id: Long): EmptyResult<DataError.Local> {
        var found = false
        _transactions.update { list ->
            val newList = list.filter {
                if (it.id == id) {
                    found = true
                    false
                } else true
            }
            newList
        }
        return if (found) Result.Success(Unit) else Result.Error(DataError.Local.NOT_FOUND)
    }
}
