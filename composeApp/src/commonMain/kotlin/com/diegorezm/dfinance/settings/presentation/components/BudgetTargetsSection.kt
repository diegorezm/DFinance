package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.settings.presentation.SettingsActions
import com.diegorezm.dfinance.settings.presentation.SettingsState
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.budget_targets_description
import dfinance.composeapp.generated.resources.budget_targets_title
import dfinance.composeapp.generated.resources.needs_label
import dfinance.composeapp.generated.resources.savings_label
import dfinance.composeapp.generated.resources.total_error
import dfinance.composeapp.generated.resources.total_label
import dfinance.composeapp.generated.resources.wants_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun BudgetTargetsSection(
    state: SettingsState,
    onAction: (SettingsActions) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
        title = stringResource(Res.string.budget_targets_title),
        description = stringResource(Res.string.budget_targets_description),
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BudgetInput(
                label = stringResource(Res.string.needs_label),
                value = state.needPercentage,
                onValueChange = {
                    onAction(SettingsActions.OnNeedPercentageChange(it))
                }
            )
            BudgetInput(
                label = stringResource(Res.string.wants_label),
                value = state.wantPercentage,
                onValueChange = {
                    onAction(SettingsActions.OnWantPercentageChange(it))
                }
            )
            BudgetInput(
                label = stringResource(Res.string.savings_label),
                value = state.savingPercentage,
                onValueChange = {
                    onAction(SettingsActions.OnSavingPercentageChange(it))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isTotalValid = state.totalPercentage == 100
                Text(
                    text = stringResource(Res.string.total_label, state.totalPercentage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isTotalValid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                )

                if (!isTotalValid) {
                    Text(
                        text = stringResource(Res.string.total_error),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
