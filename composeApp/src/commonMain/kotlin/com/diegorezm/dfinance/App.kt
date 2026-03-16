package com.diegorezm.dfinance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.theme.DFinanceTheme


@Composable
@Preview
fun App() {
    DFinanceTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Using standard Material 3 styles (already using Open Sans)
            Text(
                text = "Headline Bold (Default)",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Body Medium (Default)",
                style = MaterialTheme.typography.bodyMedium
            )

            // 2. Manual weight overrides
            Text(
                text = "Extra Bold Text",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
            )

            Text(
                text = "Light Weight Text",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Light)
            )

            // 3. Italic styles
            Text(
                text = "Italic Text",
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic)
            )

            Text(
                text = "Bold Italic Text",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}
