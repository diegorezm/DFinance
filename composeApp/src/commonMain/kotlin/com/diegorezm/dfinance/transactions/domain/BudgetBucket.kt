package com.diegorezm.dfinance.transactions.domain

import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.bucket_needs
import dfinance.composeapp.generated.resources.bucket_savings
import dfinance.composeapp.generated.resources.bucket_wants
import org.jetbrains.compose.resources.StringResource

enum class BudgetBucket {
    NEED,
    WANT,
    SAVING;

    companion object {
        fun fromString(value: String?): BudgetBucket? =
            entries.find { it.name == value }
    }
}

fun BudgetBucket.toResource(): StringResource {
    return when (this) {
        BudgetBucket.NEED -> Res.string.bucket_needs
        BudgetBucket.WANT -> Res.string.bucket_wants
        BudgetBucket.SAVING -> Res.string.bucket_savings
    }
}
