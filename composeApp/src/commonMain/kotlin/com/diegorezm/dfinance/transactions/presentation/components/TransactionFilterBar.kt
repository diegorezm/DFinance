package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.theme.DFinanceTheme
import com.diegorezm.dfinance.transactions.domain.TransactionType
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.type_all
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import dfinance.composeapp.generated.resources.type_initial
import dfinance.composeapp.generated.resources.type_transfer
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

val filterOptions =
    listOf(null, TransactionType.INCOME, TransactionType.EXPENSE, TransactionType.TRANSFER)

fun TransactionType?.labelRes(): StringResource = when (this) {
    null -> Res.string.type_all
    TransactionType.INCOME -> Res.string.type_income
    TransactionType.EXPENSE -> Res.string.type_expense
    TransactionType.TRANSFER -> Res.string.type_transfer
    TransactionType.INITIAL_BALANCE -> Res.string.type_initial
}

@Composable
fun TransactionFilterBar(
    filters: Set<TransactionType> = emptySet(),
    onFilterSelected: (TransactionType?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterOptions.forEach { option ->
            val isSelected = if (option == null) {
                filters.isEmpty()
            } else {
                filters.contains(option)
            }

            Text(
                text = stringResource(option.labelRes()),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .clickable { onFilterSelected(option) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TransactionFilterBarPreview() {
    DFinanceTheme {
        TransactionFilterBar(
            onFilterSelected = {}
        )
    }
}
