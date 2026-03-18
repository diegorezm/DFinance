package com.diegorezm.dfinance.core.domain

sealed interface DataError : Error {
    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
        NOT_FOUND,
        ALREADY_EXISTS
    }

    enum class Remote : DataError {
        SERVICE_UNAVAILABLE,
        CLIENT_ERROR,
        SERVER_ERROR,
        NETWORK,
        UNKNOWN,
        NOT_FOUND,
        UNAUTHORIZED
    }
}
