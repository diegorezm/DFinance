package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.presentation.components.toDisplayAmount
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.TransactionType
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.amount_label
import dfinance.composeapp.generated.resources.amount_placeholder
import dfinance.composeapp.generated.resources.cancel
import dfinance.composeapp.generated.resources.note_label
import dfinance.composeapp.generated.resources.note_placeholder
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import dfinance.composeapp.generated.resources.type_label
import dfinance.composeapp.generated.resources.type_transfer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionForm(
    accountId: Long,
    accounts: List<BankAccount>,
    initialAmount: Long = 0,
    initialType: TransactionType = TransactionType.EXPENSE,
    initialNote: String = "",
    initialToAccountId: Long? = null,
    submitLabel: String,
    onDismiss: () -> Unit,
    onSubmit: (TransactionDTO) -> Unit
) {
    var amountText by remember { mutableStateOf(if (initialAmount == 0L) "" else initialAmount.toDisplayAmount()) }
    var selectedType by remember { mutableStateOf(initialType) }
    var note by remember { mutableStateOf(initialNote) }
    var typeExpanded by remember { mutableStateOf(false) }

    var toAccountId by remember { mutableStateOf(initialToAccountId) }
    var toAccountExpanded by remember { mutableStateOf(false) }

    val otherAccounts = accounts.filter { it.id != accountId }

    val canSubmit = amountText.isNotBlank() &&
            amountText.toDoubleOrNull() != null &&
            (selectedType != TransactionType.TRANSFER || toAccountId != null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount
        Column {
            Text(
                text = stringResource(Res.string.amount_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                placeholder = {
                    Text(
                        stringResource(Res.string.amount_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Type
        Column {
            Text(
                text = stringResource(Res.string.type_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it },

                ) {
                OutlinedTextField(
                    value = when (selectedType) {
                        TransactionType.INCOME -> stringResource(Res.string.type_income)
                        TransactionType.EXPENSE -> stringResource(Res.string.type_expense)
                        TransactionType.TRANSFER -> stringResource(Res.string.type_transfer)
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    shape = RoundedCornerShape(0.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    TransactionType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when (type) {
                                        TransactionType.INCOME -> stringResource(Res.string.type_income)
                                        TransactionType.EXPENSE -> stringResource(Res.string.type_expense)
                                        TransactionType.TRANSFER -> stringResource(Res.string.type_transfer)
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedType = type
                                typeExpanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface
                                )
                        )
                    }
                }
            }
        }

        // Destination Account (if Transfer)
        if (selectedType == TransactionType.TRANSFER) {
            Column {
                Text(
                    text = "Transfer to", // TODO: Add to resources
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = toAccountExpanded,
                    onExpandedChange = { toAccountExpanded = it },
                ) {
                    OutlinedTextField(
                        value = accounts.find { it.id == toAccountId }?.name ?: "Select account",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toAccountExpanded) },
                        shape = RoundedCornerShape(0.dp),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = toAccountExpanded,
                        onDismissRequest = { toAccountExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        otherAccounts.forEach { account ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = account.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                onClick = {
                                    toAccountId = account.id
                                    toAccountExpanded = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                            )
                        }
                    }
                }
            }
        }

        // Note
        Column {
            Text(
                text = stringResource(Res.string.note_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = {
                    Text(
                        stringResource(Res.string.note_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    stringResource(Res.string.cancel),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    val amount = (amountText.toDoubleOrNull()?.times(100))?.toLong() ?: 0L
                    onSubmit(
                        TransactionDTO(
                            accountId = accountId,
                            subcategoryId = null,
                            toAccountId = if (selectedType == TransactionType.TRANSFER) toAccountId else null,
                            type = selectedType,
                            amount = amount,
                            note = note.trim().ifBlank { null },
                            date = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).toString()
                        )
                    )
                },
                enabled = canSubmit,
                shape = RoundedCornerShape(0.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    submitLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
