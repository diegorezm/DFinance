package com.diegorezm.dfinance.bank_accounts.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.db.DFinanceDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBankAccountRepository(
    db: DFinanceDatabase
) : BankAccountRepository {

    private val queries = db.bankAccountQueries

    override fun getAccounts(): Flow<List<BankAccount>> {
        return queries.findAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    BankAccount(
                        id = entity.id,
                        name = entity.name,
                        currencyCode = entity.currency_code,
                        balance = entity.balance,
                        icon = entity.icon,
                        color = entity.color
                    )
                }
            }
    }

    override suspend fun insertAccount(account: BankAccount) {
        queries.insert(
            name = account.name,
            currency_code = account.currencyCode,
            balance = account.balance,
            icon = account.icon,
            color = account.color
        )
    }

    override suspend fun updateAccount(account: BankAccount) {
        account.id?.let { id ->
            queries.update(
                name = account.name,
                currency_code = account.currencyCode,
                balance = account.balance,
                icon = account.icon,
                color = account.color,
                id = id
            )
        }
    }

    override suspend fun deleteAccount(id: Long) {
        queries.softDelete(id)
    }
}
