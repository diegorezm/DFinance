package com.diegorezm.dfinance

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data class Details(val id: String) : Route
}
