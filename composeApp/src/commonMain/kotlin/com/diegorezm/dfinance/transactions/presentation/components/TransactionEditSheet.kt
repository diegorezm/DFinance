package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.domain.BankAccount
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import com.diegorezm.dfinance.transactions.domain.Transaction
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.edit_transaction_title
import dfinance.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditSheet(
    transaction: Transaction,
    accounts: List<BankAccount>,
    onDismiss: () -> Unit,
    onConfirm: (TransactionDTO) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(0.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(Res.string.edit_transaction_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

        TransactionForm(
            accountId = transaction.accountId,
            accounts = accounts,
            initialAmount = transaction.amount,
            initialType = transaction.type,
            initialNote = transaction.note ?: "",
            initialToAccountId = transaction.toAccountId,
            submitLabel = stringResource(Res.string.save),
            onDismiss = onDismiss,
            onSubmit = onConfirm
        )
    }
}
