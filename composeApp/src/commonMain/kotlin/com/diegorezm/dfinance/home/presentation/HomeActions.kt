package com.diegorezm.dfinance.home.presentation

sealed interface HomeActions {
    data object OnToggleChart : HomeActions
    data class OnAccountClick(val id: Long) : HomeActions
}
