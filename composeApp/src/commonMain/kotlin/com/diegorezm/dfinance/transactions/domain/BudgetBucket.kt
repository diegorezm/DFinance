package com.diegorezm.dfinance.transactions.domain

enum class BudgetBucket {
    NEED,
    WANT,
    SAVING;

    companion object {
        fun fromString(value: String?): BudgetBucket? =
            entries.find { it.name == value }
    }
}
