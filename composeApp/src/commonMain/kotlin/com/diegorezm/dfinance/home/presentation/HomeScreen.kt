package com.diegorezm.dfinance.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.bank_accounts.presentation.components.toComposeColor
import com.diegorezm.dfinance.bank_accounts.presentation.components.toDisplayAmount
import com.diegorezm.dfinance.core.presentation.components.NeobrutalistTopAppBar
import com.diegorezm.dfinance.home.presentation.components.GoalProgressBar
import com.diegorezm.dfinance.transactions.presentation.components.TransactionChartSection
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.accounts_title
import dfinance.composeapp.generated.resources.nav_home
import dfinance.composeapp.generated.resources.total_net_worth
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAccountClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            NeobrutalistTopAppBar(
                title = stringResource(Res.string.nav_home)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NetWorthCard(totalNetWorth = state.totalNetWorth)
            }

            item {
                TransactionChartSection(
                    transactions = state.transactions,
                    expanded = state.isChartExpanded,
                    chartType = state.chartType,
                    onToggle = { viewModel.onAction(HomeActions.OnToggleChart) }
                )
            }

            if (state.budgetGoals.isNotEmpty()) {
                item {
                    BudgetGoalsSection(goals = state.budgetGoals)
                }
            }

            item {
                Text(
                    text = stringResource(Res.string.accounts_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(state.accountSummaries) { summary ->
                HomeAccountItem(
                    summary = summary,
                    onClick = { onAccountClick(summary.account.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun BudgetGoalsSection(
    goals: List<BudgetGoal>,
    modifier: Modifier = Modifier
) {
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
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Monthly Budget Goals",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            goals.forEach { goal ->
                GoalProgressBar(
                    label = goal.label,
                    spent = goal.spent,
                    target = goal.target,
                    color = Color(parseColor(goal.color))
                )
            }
        }
    }
}

private fun parseColor(hex: String): Int {
    return hex.removePrefix("#").toLong(16).toInt() or -0x1000000
}

@Composable
private fun NetWorthCard(
    totalNetWorth: Long,
    modifier: Modifier = Modifier
) {
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
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.total_net_worth),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = totalNetWorth.toDisplayAmount(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun HomeAccountItem(
    summary: AccountSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = summary.account.color.toComposeColor()

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 3.dp, y = 3.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(accentColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = summary.account.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = summary.balance.toDisplayAmount(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
