package com.diegorezm.dfinance.categories.domain

data class Subcategory(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val isDefault: Boolean
)
