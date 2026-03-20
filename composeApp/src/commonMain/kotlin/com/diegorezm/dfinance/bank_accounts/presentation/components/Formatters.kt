package com.diegorezm.dfinance.bank_accounts.presentation.components

import androidx.compose.ui.graphics.Color

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
 */
fun Long.toDisplayAmount(): String {
    val absoluteValue = if (this < 0) -this else this
    val dollars = absoluteValue / 100
    val cents = absoluteValue % 100
    val sign = if (this < 0) "-" else ""
    return "$sign$dollars.${cents.toString().padStart(2, '0')}"
}
