package com.diegorezm.dfinance.categories.domain

enum class Bucket {
    NEEDS,
    WANTS,
    SAVINGS;

    companion object {
        fun fromString(value: String): Bucket =
            entries.find { it.name == value } ?: NEEDS
    }
}
