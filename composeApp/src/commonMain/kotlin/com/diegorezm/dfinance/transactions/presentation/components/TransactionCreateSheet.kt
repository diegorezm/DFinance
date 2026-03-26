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
import com.diegorezm.dfinance.transactions.data.dto.TransactionDTO
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.create
import dfinance.composeapp.generated.resources.new_transaction_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCreateSheet(
    accountId: Long,
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
            text = stringResource(Res.string.new_transaction_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

        TransactionForm(
            accountId = accountId,
            submitLabel = stringResource(Res.string.create),
            onDismiss = onDismiss,
            onSubmit = onConfirm
        )
    }
}
