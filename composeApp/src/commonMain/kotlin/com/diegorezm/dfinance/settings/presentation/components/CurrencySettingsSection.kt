package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.Currency
import com.diegorezm.dfinance.settings.presentation.SettingsActions
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.currency_settings_description
import dfinance.composeapp.generated.resources.currency_settings_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CurrencySettingsSection(
    selectedCurrency: Currency,
    onAction: (SettingsActions) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
        title = stringResource(Res.string.currency_settings_title),
        description = stringResource(Res.string.currency_settings_description),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val currencies = listOf(Currency.BRL, Currency.USD)
            
            currencies.forEach { currency ->
                val isSelected = currency == selectedCurrency
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .clickable { onAction(SettingsActions.OnCurrencyChange(currency)) }
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onAction(SettingsActions.OnCurrencyChange(currency)) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Text(
                            text = "${currency.code} (${currency.symbol})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
