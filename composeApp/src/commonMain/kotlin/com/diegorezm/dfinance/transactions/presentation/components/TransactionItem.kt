package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.presentation.components.toFormattedCurrency
import com.diegorezm.dfinance.theme.DFinanceTheme
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType
import com.diegorezm.dfinance.transactions.domain.toResource
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.cancel
import dfinance.composeapp.generated.resources.delete
import dfinance.composeapp.generated.resources.delete_transaction_message
import dfinance.composeapp.generated.resources.delete_transaction_title
import dfinance.composeapp.generated.resources.edit
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import dfinance.composeapp.generated.resources.type_transfer
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isNoteExpanded by remember { mutableStateOf(false) }

    val hasNote = !transaction.note.isNullOrBlank()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.outline),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = stringResource(Res.string.delete_transaction_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = stringResource(Res.string.delete_transaction_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteClick()
                }) {
                    Text(
                        stringResource(Res.string.delete),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        stringResource(Res.string.cancel),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }

    val (icon, iconBg, amountColor, prefix, labelRes) = when (transaction.type) {
        TransactionType.INCOME -> Quintuple(
            Icons.Default.KeyboardArrowUp,
            Color(0xFF4CAF50),
            Color(0xFF4CAF50),
            "+",
            Res.string.type_income
        )

        TransactionType.EXPENSE -> {
            val expenseColor = when (transaction.budgetBucket) {
                BudgetBucket.SAVING -> Color(0xFF2196F3) // Blue for savings
                BudgetBucket.WANT -> Color(0xFFFF9800)   // Orange for wants
                BudgetBucket.NEED -> Color(0xFFF44336)   // Red for needs
                null -> Color(0xFFF44336)
            }
            Quintuple(
                Icons.Default.KeyboardArrowDown,
                expenseColor,
                expenseColor,
                "-",
                Res.string.type_expense
            )
        }

        TransactionType.TRANSFER -> Quintuple(
            Icons.Default.PlayArrow,
            Color(0xFF9C27B0), // Purple for transfers
            MaterialTheme.colorScheme.onSurface,
            "",
            Res.string.type_transfer
        )
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 3.dp, y = 3.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (hasNote) {
                            isNoteExpanded = !isNoteExpanded
                        } else {
                            onClick()
                        }
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(iconBg.copy(alpha = 0.15f))
                            .border(2.dp, iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (transaction.type == TransactionType.EXPENSE && transaction.budgetBucket != null) {
                            BudgetBucketIcon(
                                bucket = transaction.budgetBucket,
                                tint = iconBg,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(labelRes),
                                tint = iconBg,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (transaction.type == TransactionType.EXPENSE && transaction.budgetBucket != null) {
                                    stringResource(transaction.budgetBucket.toResource())
                                } else {
                                    stringResource(labelRes)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (hasNote) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Has note",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Text(
                            text = transaction.date.take(10),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$prefix${transaction.amount.toFormattedCurrency()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = amountColor
                    )

                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(Res.string.edit),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (isNoteExpanded && hasNote) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = transaction.note!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TransactionItemPreview() {
    DFinanceTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TransactionItem(
                transaction = Transaction(
                    id = 1L,
                    accountId = 1L,
                    subcategoryId = 1L,
                    toAccountId = null,
                    type = TransactionType.INCOME,
                    amount = 120050,
                    note = "Salary for March",
                    date = "2024-03-20T10:00:00",
                    budgetBucket = null
                ),
                onClick = {},
                onDeleteClick = {}
            )
        }
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
