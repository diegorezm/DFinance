package com.diegorezm.dfinance.transactions.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.core.presentation.components.NeobrutalistBottomFloatingFAB
import com.diegorezm.dfinance.core.presentation.components.NeobrutalistTopAppBar
import com.diegorezm.dfinance.transactions.presentation.components.AccountSummaryCard
import com.diegorezm.dfinance.transactions.presentation.components.TransactionCreateSheet
import com.diegorezm.dfinance.transactions.presentation.components.TransactionEditSheet
import com.diegorezm.dfinance.transactions.presentation.components.TransactionFilterBar
import com.diegorezm.dfinance.transactions.presentation.components.TransactionItem
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.add_first_account
import dfinance.composeapp.generated.resources.no_transactions
import dfinance.composeapp.generated.resources.transactions_title
import dfinance.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    if (state.isCreateSheetOpen) {
        TransactionCreateSheet(
            accountId = viewModel.bankId,
            onDismiss = {
                viewModel.onAction(TransactionsActions.OnDismissCreateSheet)
            },
            onConfirm = { dto ->
                viewModel.onAction(TransactionsActions.OnConfirmCreateTransaction(dto))
            }
        )
    }

    state.editingTransaction?.let { transaction ->
        TransactionEditSheet(
            transaction = transaction,
            onDismiss = {
                viewModel.onAction(TransactionsActions.OnDismissEditSheet)
            },
            onConfirm = { dto ->
                viewModel.onAction(TransactionsActions.OnConfirmEditTransaction(dto))
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            NeobrutalistTopAppBar(
                title = state.account?.name ?: stringResource(Res.string.transactions_title),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                state.error != null -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: stringResource(Res.string.unknown_error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Account summary card
                    state.account?.let { account ->
                        item {
                            AccountSummaryCard(
                                account = account,
                                totalIncome = state.totalIncome,
                                totalExpenses = state.totalExpenses
                            )
                        }
                    }

                    // Filter bar
                    item {
                        TransactionFilterBar(
                            filters = state.activeFilters,
                            onFilterSelected = {
                                viewModel.onAction(TransactionsActions.OnFilterSelected(it))
                            }
                        )
                    }

                    // Empty state
                    if (state.filteredTransactions.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(top = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(Res.string.no_transactions),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(Res.string.add_first_account),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Transaction list
                    items(state.filteredTransactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = {
                                viewModel.onAction(TransactionsActions.OnEditTransactionClick(transaction))
                            },
                            onDeleteClick = {
                                viewModel.onAction(TransactionsActions.OnDeleteTransactionClick(transaction.id))
                            }
                        )
                    }
                }
            }

            NeobrutalistBottomFloatingFAB(
                icon = Icons.Default.Add,
                onClick = {
                    viewModel.onAction(TransactionsActions.OnAddTransactionClick)
                },
            )
        }
    }
}
