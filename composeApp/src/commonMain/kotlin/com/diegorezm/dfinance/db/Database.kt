package com.diegorezm.dfinance.db

class Database(driverFactory: DatabaseDriverFactory) {
    private val db = DFinanceDatabase(driverFactory.createDriver())
    
}