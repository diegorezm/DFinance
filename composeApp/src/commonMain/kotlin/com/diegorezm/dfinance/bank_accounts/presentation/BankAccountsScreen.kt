package com.diegorezm.dfinance.bank_accounts.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BankAccountsScreen(
    viewModel: BankAccountsViewModel = koinViewModel(),
    onAddAccountClick: () -> Unit = {},
    onAccountClick: (BankAccount) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAccountClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "Unknown Error",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.accounts.isEmpty() -> {
                    Text("No accounts found. Add one!")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.accounts) { account ->
                            BankAccountItem(
                                account = account,
                                onClick = { onAccountClick(account) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BankAccountItem(
    account: BankAccount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Basic implementation of an item, you can style this according to your theme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = account.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${account.balance} ${account.currencyCode}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
