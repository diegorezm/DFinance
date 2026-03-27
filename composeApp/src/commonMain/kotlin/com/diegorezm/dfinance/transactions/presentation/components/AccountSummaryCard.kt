package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.presentation.components.toDisplayAmount
import com.diegorezm.dfinance.theme.DFinanceTheme


@Composable
fun AccountSummaryCard(
    account: BankAccount,
    totalIncome: Long,
    totalExpenses: Long,
    modifier: Modifier = Modifier
) {
    val accentColor = account.color.toComposeColor()

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 4.dp, y = 4.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(accentColor.copy(alpha = 0.08f))
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Income",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "+${totalIncome.toDisplayAmount()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Expenses",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "-${totalExpenses.toDisplayAmount()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

fun String.toComposeColor(): Color = try {
    Color(this.removePrefix("#").toLong(16) or 0xFF000000)
} catch (e: Exception) {
    Color(0xFF4CAF50)
}

@Preview
@Composable
private fun AccountSummaryCardPreview() {
    DFinanceTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AccountSummaryCard(
                account = BankAccount(
                    id = 1L,
                    name = "Main Savings",
                    currencyCode = "USD",
                    color = "#E8844A"
                ),
                totalIncome = 250000,
                totalExpenses = 100000
            )
        }
    }
}
