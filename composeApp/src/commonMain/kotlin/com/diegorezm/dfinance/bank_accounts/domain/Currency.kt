package com.diegorezm.dfinance.bank_accounts.domain

import kotlin.math.pow

enum class Currency(
    val code: String,
    val symbol: String,
    val minorUnits: Int
) {
    BRL("BRL", "R$", 2),
    USD("USD", "$", 2),
    EUR("EUR", "€", 2),
    GBP("GBP", "£", 2),
    JPY("JPY", "¥", 0),
    CAD("CAD", "CA$", 2),
    AUD("AUD", "A$", 2),
    CHF("CHF", "Fr", 2),
    CNY("CNY", "¥", 2),
    MXN("MXN", "MX$", 2);

    fun format(amountMinorUnits: Long): String {
        val initialDivisor = 10.0
        val divisor = initialDivisor.pow(minorUnits.toDouble())
        val whole = amountMinorUnits / divisor
        val fraction = amountMinorUnits % divisor
        return if (minorUnits == 0) {
            "$symbol $whole"
        } else {
            "$symbol $whole.${fraction.toString().padStart(minorUnits, '0')}"
        }
    }

    companion object {
        fun fromCode(code: String): Currency =
            entries.find { it.code == code } ?: BRL
    }
}
