package com.diegorezm.dfinance.bank_accounts.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.diegorezm.dfinance.bank_accounts.domain.Currency
import com.diegorezm.dfinance.settings.domain.SettingsKeys
import com.russhwolf.settings.Settings
import org.koin.compose.koinInject

/**
 * Converts a hex color string (e.g., "#4CAF50" or "4CAF50") to a Compose Color.
 */
fun String.toComposeColor(): Color {
    return try {
        val hex = this.removePrefix("#")
        val longValue = hex.toLong(16)
        if (hex.length <= 6) {
            // Assume RGB if 6 chars or less
            Color(longValue or 0xFF000000)
        } else {
            // Assume ARGB if 8 chars
            Color(longValue)
        }
    } catch (e: Exception) {
        Color(0xFF4CAF50) // Default green
    }
}

/**
 * Formats minor units (cents) to display string e.g. 1999 -> "19.99"
 * This version doesn't include the currency symbol.
 */
fun Long.toDisplayAmount(): String {
    val absoluteValue = if (this < 0) -this else this
    val dollars = absoluteValue / 100
    val cents = absoluteValue % 100
    val sign = if (this < 0) "-" else ""
    return "$sign$dollars.${cents.toString().padStart(2, '0')}"
}

/**
 * Formats minor units (cents) to display string with the current currency symbol.
 * e.g. 1999 -> "R$ 19.99"
 */
@Composable
fun Long.toFormattedCurrency(): String {
    val settings = koinInject<Settings>()
    val currencyCode = remember(settings) {
        settings.getString(SettingsKeys.KEY_CURRENCY, Currency.BRL.code)
    }
    val currency = remember(currencyCode) {
        Currency.fromCode(currencyCode)
    }

    return currency.format(this)
}
