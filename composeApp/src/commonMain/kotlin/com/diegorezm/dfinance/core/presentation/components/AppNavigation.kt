package com.diegorezm.dfinance.core.presentation.components

import BaselineAccountBalance
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.Route
import com.diegorezm.dfinance.theme.DFinanceTheme
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.nav_accounts
import dfinance.composeapp.generated.resources.nav_home
import dfinance.composeapp.generated.resources.nav_settings
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class NavigationItem(
    val labelRes: StringResource,
    val icon: ImageVector,
    val route: Route
)

val navigationItems = listOf(
    NavigationItem(Res.string.nav_home, Icons.Default.Home, Route.Home),
    NavigationItem(
        Res.string.nav_accounts,
        BaselineAccountBalance,
        Route.BankAccounts
    ),
    NavigationItem(Res.string.nav_settings, Icons.Default.Settings, Route.AppSettings)
)

@Composable
fun AppBottomNavigation(
    currentRoute: Route?,
    onRouteSelected: (Route) -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RectangleShape
            )
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onRouteSelected(item.route) },
                icon = {
                    Box(
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.labelRes),
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.background
                ),

                )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomNavigationPreview() {
    DFinanceTheme {
        AppBottomNavigation(currentRoute = Route.Home)
    }
}