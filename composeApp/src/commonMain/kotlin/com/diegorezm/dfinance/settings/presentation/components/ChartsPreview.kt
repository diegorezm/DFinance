package com.diegorezm.dfinance.settings.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun NeobrutalistBarChartPreview() {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom
    ) {
        val heights = listOf(0.4f, 0.7f, 0.5f, 0.9f)
        heights.forEach { height ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(height)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape)
            )
        }
    }
}

@Composable
fun NeobrutalistLineChartPreview() {
    val lineColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width * 0.3f, size.height * 0.5f)
            lineTo(size.width * 0.6f, size.height * 0.7f)
            lineTo(size.width, size.height * 0.1f)
        }

        // Draw the thick Neobrutalist stroke
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 8.dp.toPx(),
                cap = StrokeCap.Square,
                join = StrokeJoin.Miter
            )
        )
        // Draw an even thicker black border behind it to give it that raw outline effect (optional)
        drawPath(
            path = path,
            color = outlineColor,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Square, join = StrokeJoin.Miter)
        )
    }
}

@Composable
fun NeobrutalistPieChartPreview() {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val outlineColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        // Main Slice
        drawArc(
            color = primaryColor,
            startAngle = 0f,
            sweepAngle = 240f,
            useCenter = true
        )
        // Secondary Slice
        drawArc(
            color = secondaryColor,
            startAngle = 240f,
            sweepAngle = 120f,
            useCenter = true
        )
        // Harsh Borders
        drawArc(
            color = outlineColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = Stroke(width = 4.dp.toPx())
        )
        // Inner dividing line
        drawLine(
            color = outlineColor,
            start = center,
            end = center.copy(x = center.x + size.width / 2),
            strokeWidth = 4.dp.toPx()
        )
    }
}