package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.theme.DFinanceTheme
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import dfinance.composeapp.generated.resources.type_initial_balance
import dfinance.composeapp.generated.resources.type_transfer
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val (icon, iconBg, amountColor, prefix, labelRes) = when (transaction.type) {
        TransactionType.INCOME -> Quintuple(
            Icons.Default.KeyboardArrowDown,
            Color(0xFF4CAF50),
            Color(0xFF4CAF50),
            "+",
            Res.string.type_income
        )

        TransactionType.EXPENSE -> Quintuple(
            Icons.Default.KeyboardArrowUp,
            Color(0xFFF44336),
            Color(0xFFF44336),
            "-",
            Res.string.type_expense
        )

        TransactionType.TRANSFER -> Quintuple(
            Icons.Default.PlayArrow,
            Color(0xFF2196F3),
            MaterialTheme.colorScheme.onSurface,
            "",
            Res.string.type_transfer
        )

        TransactionType.INITIAL_BALANCE -> Quintuple(
            Icons.Default.KeyboardArrowDown,
            Color(0xFF4CAF50),
            Color(0xFF4CAF50),
            "+",
            Res.string.type_initial_balance
        )
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 3.dp, y = 3.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBg.copy(alpha = 0.15f))
                        .border(2.dp, iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(labelRes),
                        tint = iconBg,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = transaction.date.take(10),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "$prefix${transaction.amount.toDisplayAmount()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
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
                    note = "Salary",
                    date = "2024-03-20T10:00:00"
                ),
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

fun Long.toDisplayAmount(): String {
    val whole = this / 100
    val fraction = this % 100
    return "$whole.${fraction.toString().padStart(2, '0')}"
}
