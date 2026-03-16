package com.diegorezm.dfinance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.diegorezm.dfinance.theme.DFinanceTheme

@Composable
@Preview
fun App() {
    DFinanceTheme {
        val backStack = remember { mutableStateListOf<Route>(Route.Home) }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Route.Home> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    ) {
                        Text("Home Screen")
                        Button(onClick = { backStack.add(Route.Details("123")) }) {
                            Text("Go to Details")
                        }
                    }
                }
                entry<Route.Details> { key ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    ) {
                        Text("Details Screen for ID: ${key.id}")
                        Button(onClick = { backStack.removeLastOrNull() }) {
                            Text("Go Back")
                        }
                    }
                }
            }
        )
    }
}
