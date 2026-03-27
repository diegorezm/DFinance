package com.diegorezm.dfinance.bank_accounts.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.diegorezm.dfinance.bank_accounts.data.dto.BankAccountDTO
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.core.domain.DataError
import com.diegorezm.dfinance.core.domain.EmptyResult
import com.diegorezm.dfinance.core.domain.Result
import com.diegorezm.dfinance.db.DFinanceDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBankAccountRepository(
    private val db: DFinanceDatabase
) : BankAccountRepository {

    private val queries = db.bankAccountQueries
    private val transactionsQueries = db.transactionQueries

    override fun findAll(): Flow<List<BankAccount>> {
        return queries.findAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    BankAccount(
                        id = entity.id,
                        name = entity.name,
                        currencyCode = entity.currency_code,
                        color = entity.color
                    )
                }
            }
    }

    override fun findById(id: Long): Result<BankAccount, DataError.Local> {
        return try {
            val result = queries.findById(id).executeAsOneOrNull()
            if (result != null) {
                return Result.Success(
                    BankAccount(
                        id = result.id,
                        name = result.name,
                        currencyCode = result.currency_code,
                        color = result.color
                    )
                )
            }
            Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }

    }

    override suspend fun create(account: BankAccountDTO): EmptyResult<DataError.Local> {
        return try {
            db.transaction {
                queries.insert(
                    name = account.name,
                    currency_code = account.currencyCode,
                    color = account.color
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun update(account: BankAccount): EmptyResult<DataError.Local> {
        return try {
            val id = account.id
            queries.update(
                name = account.name,
                currency_code = account.currencyCode,
                color = account.color,
                id = id
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun delete(id: Long): EmptyResult<DataError.Local> {
        return try {
            queries.softDelete(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
