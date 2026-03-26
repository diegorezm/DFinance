package com.diegorezm.dfinance.categories.domain

data class Category(
    val id: Long,
    val name: String,
    val bucket: Bucket,
    val color: String,
    val isDefault: Boolean
)