package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.settings.presentation.SettingsActions
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.chart_settings_description
import dfinance.composeapp.generated.resources.chart_settings_title
import dfinance.composeapp.generated.resources.chart_type_bar
import dfinance.composeapp.generated.resources.chart_type_pie
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChartSettingsSection(
    selectedChartType: ChartType,
    onAction: (SettingsActions) -> Unit,
    modifier: Modifier = Modifier
) {
    val chartTypes = ChartType.entries
    val pagerState = rememberPagerState(
        initialPage = chartTypes.indexOf(selectedChartType).coerceAtLeast(0),
        pageCount = { chartTypes.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAction(SettingsActions.OnChartTypeChange(chartTypes[page]))
        }
    }

    SettingsSection(
        title = stringResource(Res.string.chart_settings_title),
        description = stringResource(Res.string.chart_settings_description),
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val chartType = chartTypes[page]
                val isSelected = selectedChartType == chartType

                ChartPlaceholder(
                    chartType = chartType,
                    isSelected = isSelected,
                    onClick = {
                        // Optional: allow clicking to scroll to page if needed,
                        // but HorizontalPager handles swipe already.
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (selectedChartType) {
                    ChartType.BAR -> stringResource(Res.string.chart_type_bar)
                    ChartType.PIE -> stringResource(Res.string.chart_type_pie)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ChartPlaceholder(
    chartType: ChartType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RectangleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Call the natively drawn shapes!
        Box(modifier = Modifier.padding(16.dp)) {
            when (chartType) {
                ChartType.BAR -> NeobrutalistBarChartPreview()
                ChartType.PIE -> NeobrutalistPieChartPreview()
            }
        }
    }
}
