package com.diegorezm.dfinance.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.core.presentation.components.NeobrutalistTopAppBar
import com.diegorezm.dfinance.settings.presentation.components.BudgetTargetsSection
import com.diegorezm.dfinance.settings.presentation.components.ChartSettingsSection
import com.diegorezm.dfinance.settings.presentation.components.CurrencySettingsSection
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.nav_settings
import dfinance.composeapp.generated.resources.settings_saved_success
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()
    val successMessage = stringResource(Res.string.settings_saved_success)

    LaunchedEffect(state.showSaveSuccess) {
        if (state.showSaveSuccess) {
            snackbarHostState.showSnackbar(successMessage)
            viewModel.onAction(SettingsActions.OnDismissSuccessMessage)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            NeobrutalistTopAppBar(
                title = stringResource(Res.string.nav_settings)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ChartSettingsSection(
                        selectedChartType = state.chartType,
                        onAction = viewModel::onAction
                    )
                }

                item {
                    CurrencySettingsSection(
                        selectedCurrency = state.currency,
                        onAction = viewModel::onAction
                    )
                }

                item {
                    BudgetTargetsSection(
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}
