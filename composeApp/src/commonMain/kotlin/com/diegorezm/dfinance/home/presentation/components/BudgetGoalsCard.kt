package com.diegorezm.dfinance.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType

@Composable
fun BudgetGoalsCard(
    transactions: List<Transaction>,
    needsPct: Int,
    wantsPct: Int,
    savingsPct: Int,
    modifier: Modifier = Modifier
) {
    // 1. Calculate the Faucet (Total Income)
    val totalIncome = transactions
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }

    // 2. Calculate Targets based on Income
    val needsTarget = (totalIncome * needsPct) / 100
    val wantsTarget = (totalIncome * wantsPct) / 100
    val savingsTarget = (totalIncome * savingsPct) / 100

    // 3. Calculate Actuals (What was put into the buckets)
    val needsSpent =
        transactions.filter { it.budgetBucket == BudgetBucket.NEED }.sumOf { it.amount }
    val wantsSpent =
        transactions.filter { it.budgetBucket == BudgetBucket.WANT }.sumOf { it.amount }
    val savingsSpent =
        transactions.filter { it.budgetBucket == BudgetBucket.SAVING }.sumOf { it.amount }

    // 4. Draw the Neobrutalist Card
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "THIS MONTH'S GOALS",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Only show the bars if there is income. Otherwise, targets are 0.
        if (totalIncome > 0) {
            GoalProgressBar(
                label = "NEEDS ($needsPct%)",
                spent = needsSpent,
                target = needsTarget,
                color = MaterialTheme.colorScheme.primary
            )
            GoalProgressBar(
                label = "WANTS ($wantsPct%)",
                spent = wantsSpent,
                target = wantsTarget,
                color = MaterialTheme.colorScheme.secondary // Or a different accent color
            )
            GoalProgressBar(
                label = "SAVINGS ($savingsPct%)",
                spent = savingsSpent,
                target = savingsTarget,
                color = MaterialTheme.colorScheme.tertiary // Or a different accent color
            )
        } else {
            // Empty State
            Text(
                text = "Add your first income this month to see your budget targets.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}