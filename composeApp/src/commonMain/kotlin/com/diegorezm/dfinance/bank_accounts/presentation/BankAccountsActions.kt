package com.diegorezm.dfinance.bank_accounts.presentation

import com.diegorezm.dfinance.bank_accounts.data.dto.BankAccountDTO
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount

sealed interface BankAccountsActions {
    object OnAddAccountClick : BankAccountsActions
    data class OnAccountClick(val account: BankAccount) : BankAccountsActions
    data class OnEditAccountClick(val account: BankAccount) : BankAccountsActions
    data class OnDeleteAccountClick(val id: Long) : BankAccountsActions
    object OnDismissCreateSheet : BankAccountsActions
    data class OnConfirmCreateAccount(val dto: BankAccountDTO) : BankAccountsActions
    object OnDismissEditSheet : BankAccountsActions
    data class OnConfirmEditAccount(val dto: BankAccountDTO) : BankAccountsActions
}
