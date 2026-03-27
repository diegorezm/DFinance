package com.diegorezm.dfinance.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GoalProgressBar(
    label: String,
    spent: Long,
    target: Long,
    color: Color
) {
    // Avoid division by zero
    val safeTarget = if (target == 0L) 1L else target
    val progressFraction = (spent.toFloat() / safeTarget.toFloat()).coerceIn(0f, 1f)
    val isOverBudget = spent > target

    // Use a warning color if they busted the budget
    val barColor = if (isOverBudget) MaterialTheme.colorScheme.error else color

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            // You can replace this formatting with your toDisplayAmount() extension!
            Text(
                text = "${spent / 100} / ${target / 100}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        }

        // The chunky bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progressFraction)
                    .background(barColor)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RectangleShape
                    ) // Adds a harsh line at the end of the progress fill
            )
        }
    }
}