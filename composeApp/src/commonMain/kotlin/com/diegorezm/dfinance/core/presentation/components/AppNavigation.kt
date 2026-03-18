package com.diegorezm.dfinance.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.Route

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: Route
)

val navigationItems = listOf(
    NavigationItem("Home", Icons.Default.Home, Route.Home),
    NavigationItem("Accounts", Icons.Rounded.ShoppingCart, Route.BankAccounts),
    NavigationItem("Settings", Icons.Default.Settings, Route.AppSettings)
)

@Composable
fun AppBottomNavigation(
    currentRoute: Route?,
    onRouteSelected: (Route) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        modifier = Modifier.border(
            width = 1.dp,
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
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.surface // Remove the rounded pill indicator
                )
            )
        }
    }
}
