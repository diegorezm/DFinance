package com.diegorezm.dfinance.transactions.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.core.domain.Result
import com.diegorezm.dfinance.db.DFinanceDatabase
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionRepository
import com.diegorezm.dfinance.transactions.domain.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultTransactionRepository(private val db: DFinanceDatabase) : TransactionRepository {
    private val queries = db.transactionQueries

    override fun findAll(): Flow<List<Transaction>> =
        queries.findAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }

    override fun findByAccountId(accountId: Long): Flow<List<Transaction>> =
        queries.findByAccountId(accountId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }

    override fun findByMonth(accountId: Long, month: String): Flow<List<Transaction>> =
        queries.findByAccountIdAndMonth(accountId, month)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }

    override fun findByDateBetween(
        accountId: Long,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>> =
        queries.findByDateBetween(accountId, startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun create(dto: TransactionDTO): EmptyResult<DataError.Local> {
        return try {
            queries.insert(
                account_id = dto.accountId,
                subcategory_id = dto.subcategoryId,
                to_account_id = dto.toAccountId,
                type = dto.type.name,
                amount = dto.amount,
                note = dto.note,
                date = dto.date
            )

            if (dto.type == TransactionType.TRANSFER && dto.toAccountId != null) {
                queries.insert(
                    account_id = dto.toAccountId,
                    subcategory_id = null,
                    to_account_id = dto.accountId,
                    type = TransactionType.INCOME.name,
                    amount = dto.amount,
                    note = dto.note,
                    date = dto.date
                )
                recalculateBalance(dto.toAccountId)
            }

            recalculateBalance(dto.accountId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun update(id: Long, dto: TransactionDTO): EmptyResult<DataError.Local> {
        return try {
            queries.update(
                subcategory_id = dto.subcategoryId,
                type = dto.type.name,
                amount = dto.amount,
                note = dto.note,
                date = dto.date,
                id = id
            )
            recalculateBalance(dto.accountId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun delete(id: Long): EmptyResult<DataError.Local> {
        return try {
            val transaction = queries.findAll().executeAsList().find { it.id == id }
                ?: return Result.Error(DataError.Local.NOT_FOUND)
            queries.softDelete(id)
            recalculateBalance(transaction.account_id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    private fun recalculateBalance(accountId: Long) {
        val transactions = queries
            .findByAccountId(accountId)
            .executeAsList()

        transactions.sumOf { t ->
            when (TransactionType.fromString(t.type)) {
                TransactionType.INCOME -> t.amount
                TransactionType.EXPENSE -> -t.amount
                TransactionType.TRANSFER -> 0L
            }
        }
    }

    private fun com.diegorezm.dfinance.db.TransactionEntity.toDomain(): Transaction =
        Transaction(
            id = id,
            accountId = account_id,
            subcategoryId = subcategory_id,
            toAccountId = to_account_id,
            type = TransactionType.fromString(type),
            amount = amount,
            note = note,
            date = date
        )
}
