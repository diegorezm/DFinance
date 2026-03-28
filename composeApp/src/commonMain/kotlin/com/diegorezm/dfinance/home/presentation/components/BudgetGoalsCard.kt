package com.diegorezm.dfinance.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.budget_goals_title
import dfinance.composeapp.generated.resources.empty_budget_goals_message
import dfinance.composeapp.generated.resources.needs_label_pct
import dfinance.composeapp.generated.resources.savings_label_pct
import dfinance.composeapp.generated.resources.wants_label_pct
import org.jetbrains.compose.resources.stringResource

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
            text = stringResource(Res.string.budget_goals_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (totalIncome > 0) {
            GoalProgressBar(
                label = stringResource(Res.string.needs_label_pct, needsPct).uppercase(),
                spent = needsSpent,
                target = needsTarget,
                color = MaterialTheme.colorScheme.primary
            )
            GoalProgressBar(
                label = stringResource(Res.string.wants_label_pct, wantsPct).uppercase(),
                spent = wantsSpent,
                target = wantsTarget,
                color = MaterialTheme.colorScheme.secondary
            )
            GoalProgressBar(
                label = stringResource(Res.string.savings_label_pct, savingsPct).uppercase(),
                spent = savingsSpent,
                target = savingsTarget,
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            // Empty State
            Text(
                text = stringResource(Res.string.empty_budget_goals_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
