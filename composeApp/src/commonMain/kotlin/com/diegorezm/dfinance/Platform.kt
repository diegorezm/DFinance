package com.diegorezm.dfinance

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform