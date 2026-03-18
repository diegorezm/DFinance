package com.diegorezm.dfinance.di

import com.diegorezm.dfinance.bank_accounts.data.repositories.DefaultBankAccountRepository
import com.diegorezm.dfinance.bank_accounts.domain.BankAccountRepository
import com.diegorezm.dfinance.bank_accounts.presentation.BankAccountsViewModel
import com.diegorezm.dfinance.db.Database
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { Database(get()).instance }
    single<BankAccountRepository> { DefaultBankAccountRepository(get()) }

    viewModelOf(::BankAccountsViewModel)
}
