package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.diegorezm.dfinance.settings.domain.ChartType
import com.diegorezm.dfinance.transactions.domain.BudgetBucket
import com.diegorezm.dfinance.transactions.domain.Transaction
import com.diegorezm.dfinance.transactions.domain.TransactionType
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.chart_collapse
import dfinance.composeapp.generated.resources.chart_expand
import dfinance.composeapp.generated.resources.chart_title
import dfinance.composeapp.generated.resources.no_chart_data
import dfinance.composeapp.generated.resources.type_expense
import dfinance.composeapp.generated.resources.type_income
import dfinance.composeapp.generated.resources.type_savings
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.Pie
import org.jetbrains.compose.resources.stringResource

private val incomeColor = Color(0xFF4CAF50)
private val expenseColor = Color(0xFFF44336)
private val savingColor = Color(0xFF2196F3)

@Composable
fun TransactionChartSection(
    transactions: List<Transaction>,
    expanded: Boolean,
    chartType: ChartType,
    onToggle: () -> Unit
) {
    val incomeLabel = stringResource(Res.string.type_income)
    val expenseLabel = stringResource(Res.string.type_expense)
    val savingLabel = stringResource(Res.string.type_savings)

    val chartData = remember(transactions, incomeLabel, expenseLabel, savingLabel) {
        buildChartData(transactions, incomeLabel, expenseLabel, savingLabel)
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

            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                contentDescription = if (expanded) stringResource(Res.string.chart_collapse) else stringResource(
                    Res.string.chart_expand
                ),
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    when (chartType) {
                        ChartType.BAR -> {
                            ColumnChart(
                                modifier = Modifier.fillMaxSize(),
                                data = remember(chartData) { chartData },
                                barProperties = BarProperties(
                                    spacing = 4.dp,
                                    thickness = 12.dp
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

                        ChartType.LINE -> {
                            val lineData = remember(chartData) {
                                listOf(
                                    Line(
                                        label = incomeLabel,
                                        values = chartData.map { it.values[0].value },
                                        color = SolidColor(incomeColor),
                                        firstGradientFillColor = incomeColor.copy(alpha = 0.2f),
                                        secondGradientFillColor = Color.Transparent
                                    ),
                                    Line(
                                        label = expenseLabel,
                                        values = chartData.map { it.values[1].value },
                                        color = SolidColor(expenseColor),
                                        firstGradientFillColor = expenseColor.copy(alpha = 0.2f),
                                        secondGradientFillColor = Color.Transparent
                                    ),
                                    Line(
                                        label = savingLabel,
                                        values = chartData.map { it.values[2].value },
                                        color = SolidColor(savingColor),
                                        firstGradientFillColor = savingColor.copy(alpha = 0.2f),
                                        secondGradientFillColor = Color.Transparent
                                    )
                                )
                            }
                            LineChart(
                                modifier = Modifier.fillMaxSize(),
                                data = lineData,
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
                                    labels = chartData.map { it.label },
                                    textStyle = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            )
                        }

                        ChartType.PIE -> {
                            val totalIncome = chartData.sumOf { it.values[0].value }
                            val totalExpenses = chartData.sumOf { it.values[1].value }
                            val totalSavings = chartData.sumOf { it.values[2].value }

                            val pieData = remember(totalIncome, totalExpenses, totalSavings) {
                                listOf(
                                    Pie(
                                        label = incomeLabel,
                                        data = totalIncome,
                                        color = incomeColor
                                    ),
                                    Pie(
                                        label = expenseLabel,
                                        data = totalExpenses,
                                        color = expenseColor
                                    ),
                                    Pie(
                                        label = savingLabel,
                                        data = totalSavings,
                                        color = savingColor
                                    )
                                )
                            }
                            PieChart(
                                modifier = Modifier.fillMaxSize(),
                                data = pieData
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun buildChartData(
    transactions: List<Transaction>,
    incomeLabel: String,
    expenseLabel: String,
    savingLabel: String
): List<Bars> {
    // Group transactions by "YYYY-MM"
    val grouped = transactions
        .filter { it.type == TransactionType.INCOME || it.type == TransactionType.EXPENSE || it.type == TransactionType.TRANSFER }
        .sortedBy { it.date }
        .groupBy { it.date.take(7) }
        .takeLast(6)
        .toMap()


    return grouped.map { (month, txs) ->
        val rawIncome = txs
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
            .toDouble() / 100.0

        val expenses = txs
            .filter { (it.type == TransactionType.EXPENSE && it.budgetBucket != BudgetBucket.SAVING) || it.type == TransactionType.TRANSFER }
            .sumOf { it.amount }
            .toDouble() / 100.0

        val savings = txs
            .filter { it.type == TransactionType.EXPENSE && it.budgetBucket == BudgetBucket.SAVING }
            .sumOf { it.amount }
            .toDouble() / 100.0
            
        val netIncome = rawIncome - expenses - savings

        Bars(
            label = month.takeLast(2).trimStart('0').ifEmpty { "0" } + "/" + month.take(4)
                .takeLast(2),
            values = listOf(
                Bars.Data(
                    label = incomeLabel,
                    value = netIncome,
                    color = SolidColor(incomeColor)
                ),
                Bars.Data(
                    label = expenseLabel,
                    value = expenses,
                    color = SolidColor(expenseColor)
                ),
                Bars.Data(
                    label = savingLabel,
                    value = savings,
                    color = SolidColor(savingColor)
                )
            )
        )
    }
}

private fun <K, V> Map<K, V>.takeLast(n: Int): Map<K, V> {
    return entries.toList().takeLast(n).associate { it.key to it.value }
}
