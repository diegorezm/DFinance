package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.diegorezm.dfinance.transactions.domain.BudgetBucket

@Composable
fun BudgetBucketIcon(
    bucket: BudgetBucket,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null
) {
    Icon(
        imageVector = bucket.toIcon(),
        contentDescription = contentDescription ?: bucket.name,
        modifier = modifier,
        tint = tint
    )
}

fun BudgetBucket.toIcon(): ImageVector {
    return when (this) {
        BudgetBucket.NEED -> Icons.Default.Favorite
        BudgetBucket.WANT -> Icons.Default.ShoppingCart
        BudgetBucket.SAVING -> Icons.Default.Star
    }
}
