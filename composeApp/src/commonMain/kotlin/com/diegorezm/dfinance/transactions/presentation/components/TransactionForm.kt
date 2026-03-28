package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.bank_accounts.presentation.components.toDisplayAmount
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.TransactionType
import com.diegorezm.dfinance.transactions.domain.toResource
import dfinance.composeapp.generated.resources.*
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
    initialBudgetBucket: BudgetBucket? = null,
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

    var selectedBudgetBucket by remember { mutableStateOf(initialBudgetBucket) }

    val otherAccounts = accounts.filter { it.id != accountId }

    val canSubmit = amountText.isNotBlank() &&
            amountText.toDoubleOrNull() != null &&
            (selectedType != TransactionType.TRANSFER || toAccountId != null) &&
            (selectedType != TransactionType.EXPENSE || selectedBudgetBucket != null)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(16.dp),
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

        // Budget Bucket (if Expense)
        if (selectedType == TransactionType.EXPENSE) {
            Column {
                Text(
                    text = stringResource(Res.string.budget_bucket_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BudgetBucket.entries.forEach { bucket ->
                        val isSelected = selectedBudgetBucket == bucket
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(2.dp, MaterialTheme.colorScheme.outline)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                )
                                .clickable { selectedBudgetBucket = bucket }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                BudgetBucketIcon(
                                    bucket = bucket,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(bucket.toResource()),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Destination Account (if Transfer)
        if (selectedType == TransactionType.TRANSFER) {
            Column {
                Text(
                    text = stringResource(Res.string.transfer_to_label),
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
                        value = accounts.find { it.id == toAccountId }?.name ?: stringResource(Res.string.select_account_placeholder),
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
                                .toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                            budgetBucket = if (selectedType == TransactionType.EXPENSE) selectedBudgetBucket else null
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
