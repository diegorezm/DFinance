package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.presentation.SettingsActions
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.chart_settings_description
import dfinance.composeapp.generated.resources.chart_settings_title
import dfinance.composeapp.generated.resources.chart_type_bar
import dfinance.composeapp.generated.resources.chart_type_line
import dfinance.composeapp.generated.resources.chart_type_pie
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChartSettingsSection(
    selectedChartType: ChartType,
    onAction: (SettingsActions) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
        title = stringResource(Res.string.chart_settings_title),
        description = stringResource(Res.string.chart_settings_description),
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ChartTypeOption(
                label = stringResource(Res.string.chart_type_bar),
                selected = selectedChartType == ChartType.BAR,
                onClick = { onAction(SettingsActions.OnChartTypeChange(ChartType.BAR)) }
            )
            ChartTypeOption(
                label = stringResource(Res.string.chart_type_line),
                selected = selectedChartType == ChartType.LINE,
                onClick = { onAction(SettingsActions.OnChartTypeChange(ChartType.LINE)) }
            )
            ChartTypeOption(
                label = stringResource(Res.string.chart_type_pie),
                selected = selectedChartType == ChartType.PIE,
                onClick = { onAction(SettingsActions.OnChartTypeChange(ChartType.PIE)) }
            )
        }
    }
}

@Composable
private fun ChartTypeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                .border(2.dp, MaterialTheme.colorScheme.outline)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
