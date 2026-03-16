package com.diegorezm.dfinance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diegorezm.dfinance.theme.DFinanceTheme


@Composable
@Preview
fun App() {
    DFinanceTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Hello World", color = MaterialTheme.colorScheme.surface)

        }
    }
}