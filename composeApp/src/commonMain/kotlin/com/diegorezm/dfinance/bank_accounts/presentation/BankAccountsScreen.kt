package com.diegorezm.dfinance.bank_accounts.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.presentation.components.BankAccountCreateSheet
import com.diegorezm.dfinance.bank_accounts.presentation.components.BankAccountItem
import com.diegorezm.dfinance.bank_accounts.presentation.components.BankAccountsEmptyState
import com.diegorezm.dfinance.bank_accounts.presentation.components.NeobrutalistFAB
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.add_account
import dfinance.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BankAccountsScreen(
    viewModel: BankAccountsViewModel = koinViewModel(),
    onAccountClick: (BankAccount) -> Unit = {},
    onEditAccountClick: (BankAccount) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    if (state.isCreateSheetOpen) {
        BankAccountCreateSheet(
            onDismiss = {
                viewModel.onAction(BankAccountsActions.OnDismissCreateSheet)
            },
            onConfirm = { accountDTO ->
                viewModel.onAction(BankAccountsActions.OnConfirmCreateAccount(accountDTO))
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            NeobrutalistFAB(
                icon = Icons.Default.Add,
                onClick = {
                    viewModel.onAction(BankAccountsActions.OnAddAccountClick)
                },
                contentDescription = stringResource(Res.string.add_account)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()

                state.error != null -> Text(
                    text = state.error ?: stringResource(Res.string.unknown_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )

                state.accounts.isEmpty() -> {
                    BankAccountsEmptyState()
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.accounts) { account ->
                        BankAccountItem(
                            account = account,
                            onClick = { onAccountClick(account) },
                            onEditClick = { onEditAccountClick(account) },
                            onDeleteClick = {
                                if (account.id != null) {
                                    viewModel.onAction(
                                        BankAccountsActions.OnDeleteAccountClick(account.id)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
