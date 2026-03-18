package com.diegorezm.dfinance.db

class Database(driverFactory: DatabaseDriverFactory) {
    val instance = DFinanceDatabase(driverFactory.createDriver())
}