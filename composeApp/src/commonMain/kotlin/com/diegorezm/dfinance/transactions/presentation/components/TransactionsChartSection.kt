package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.chart_collapse
import dfinance.composeapp.generated.resources.chart_expand
import dfinance.composeapp.generated.resources.chart_title
import dfinance.composeapp.generated.resources.no_chart_data
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelProperties
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

private val incomeColor = Color(0xFF4CAF50)
private val expenseColor = Color(0xFFF44336)

@Composable
fun TransactionChartSection(
    transactions: List<Transaction>,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val incomeLabel = stringResource(Res.string.type_income)
    val expenseLabel = stringResource(Res.string.type_expense)
    
    val chartData = remember(transactions, incomeLabel, expenseLabel) {
        buildChartData(transactions, incomeLabel, expenseLabel)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        // Header / toggle row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onToggle() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.chart_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(incomeColor)
                    )
                    Text(
                        text = stringResource(Res.string.type_income),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(expenseColor)
                    )
                    Text(
                        text = stringResource(Res.string.type_expense),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = if (expanded) stringResource(Res.string.chart_collapse) else stringResource(Res.string.chart_expand),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Chart
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            if (chartData.isEmpty()) {
                Text(
                    text = stringResource(Res.string.no_chart_data),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            } else {
                ColumnChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    data = remember(chartData) { chartData },
                    barProperties = BarProperties(
                        spacing = 4.dp,
                        thickness = 16.dp
                    ),
                    gridProperties = GridProperties(
                        enabled = true,
                        xAxisProperties = GridProperties.AxisProperties(enabled = false)
                    ),
                    indicatorProperties = HorizontalIndicatorProperties(
                        enabled = true,
                        textStyle = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        count = IndicatorCount.CountBased(4)
                    ),
                    labelProperties = LabelProperties(
                        enabled = true,
                        textStyle = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                )
            }
        }
    }
}

private fun buildChartData(
    transactions: List<Transaction>,
    incomeLabel: String,
    expenseLabel: String
): List<Bars> {
    // Group transactions by "YYYY-MM"
    val grouped = transactions
        .filter { it.type == TransactionType.INCOME || it.type == TransactionType.EXPENSE }
        .sortedBy { it.date }
        .groupBy { it.date.take(7) }
        .takeLast(6)
        .toMap()


    return grouped.map { (month, txs) ->
        val income = txs
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
            .toDouble() / 100.0

        val expenses = txs
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
            .toDouble() / 100.0

        Bars(
            label = month.takeLast(2).trimStart('0').ifEmpty { "0" } + "/" + month.take(4)
                .takeLast(2),
            values = listOf(
                Bars.Data(
                    label = incomeLabel,
                    value = income,
                    color = SolidColor(incomeColor)
                ),
                Bars.Data(
                    label = expenseLabel,
                    value = expenses,
                    color = SolidColor(expenseColor)
                )
            )
        )
    }
}

private fun <K, V> Map<K, V>.takeLast(n: Int): Map<K, V> {
    return entries.toList().takeLast(n).associate { it.key to it.value }
}
