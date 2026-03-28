package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import dfinance.composeapp.generated.resources.needs_label_pct
import dfinance.composeapp.generated.resources.savings_label_pct
import dfinance.composeapp.generated.resources.total_error
import dfinance.composeapp.generated.resources.total_label
import dfinance.composeapp.generated.resources.wants_label_pct
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
                label = stringResource(Res.string.needs_label_pct, state.needPercentage),
                value = state.needPercentage,
                onValueChange = {
                    onAction(SettingsActions.OnNeedPercentageChange(it))
                }
            )

            BudgetInput(
                label = stringResource(Res.string.wants_label_pct, state.wantPercentage),
                value = state.wantPercentage,
                onValueChange = {
                    onAction(SettingsActions.OnWantPercentageChange(it))
                }
            )

            BudgetInput(
                label = stringResource(Res.string.savings_label_pct, state.savingPercentage),
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

            AnimatedVisibility(visible = state.isModified) {
                SaveButton(
                    onClick = { onAction(SettingsActions.OnSaveClick) },
                    enabled = state.totalPercentage == 100,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
