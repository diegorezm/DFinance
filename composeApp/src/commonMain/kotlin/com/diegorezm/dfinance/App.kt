package com.diegorezm.dfinance

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.diegorezm.dfinance.bank_accounts.presentation.BankAccountsScreen
import com.diegorezm.dfinance.core.presentation.components.AppBottomNavigation
import com.diegorezm.dfinance.home.presentation.HomeScreen
import com.diegorezm.dfinance.settings.presentation.SettingsScreen
import com.diegorezm.dfinance.theme.DFinanceTheme
import com.diegorezm.dfinance.transactions.presentation.TransactionsScreen
import com.diegorezm.dfinance.transactions.presentation.TransactionsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Preview
@Composable
fun App() {
    DFinanceTheme {
        val backStack = remember { mutableStateListOf<Route>(Route.Home) }
        val snackbarHostState = remember { SnackbarHostState() }
        val currentRoute = backStack.lastOrNull()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                AppBottomNavigation(
                    currentRoute = currentRoute,
                    onRouteSelected = { route ->
                        if (route != currentRoute) {
                            backStack.clear()
                            backStack.add(route)
                        }
                    }
                )
            }
        ) {
            NavDisplay(
                modifier = Modifier
                    .fillMaxSize(),
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLast()
                    }
                },
                transitionSpec = {
                    val enterTransition = slideInHorizontally { it } + fadeIn()
                    val exitTransition = slideOutHorizontally { -it } + fadeOut()

                    ContentTransform(enterTransition, exitTransition)
                },
                entryProvider = entryProvider {
                    entry<Route.Home> {
                        HomeScreen(
                            onAccountClick = { accountId ->
                                backStack.add(Route.Transactions(accountId))
                            },
                        )
                    }
                    entry<Route.BankAccounts> {
                        BankAccountsScreen(
                            onAccountClick = {
                                backStack.add(Route.Transactions(it.id))
                            }
                        )
                    }
                    entry<Route.Transactions> { params ->
                        val viewModel: TransactionsViewModel = koinViewModel(
                            key = "transactions_${params.bankId}"
                        ) {
                            parametersOf(params.bankId)
                        }
                        TransactionsScreen(viewModel, onBackClick = {
                            backStack.removeLast()
                        })
                    }
                    entry<Route.AppSettings> {
                        SettingsScreen(
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            )
        }
    }
}
