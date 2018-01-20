package com.lavans.kotmi

import com.fasterxml.jackson.module.kotlin.readValue
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

object DataSource {
    private val log = Logger.getLogger(this)

    fun connect(db: DatabaseConfig): Database {
        val config = HikariConfig()
        config.jdbcUrl = db.url
        config.driverClassName = db.driver
        config.username = db.user
        config.password = db.pass
        val ds = HikariDataSource(config)
        log.info("DataSource:connect $ds")
        return Database.connect(ds)
    }

    data class DatabaseConfig(
        val url: String,
        val driver: String,
        val user: String,
        val pass: String
    ) {
        companion object {
            private val om = JsonObjectMapper.mapper()
            fun load(filename:String = "database.json"): DatabaseConfig {
                return om.readValue(File.resourceAsString(filename))
            }
        }
    }

    fun <T>transactionWithLog(f:() -> T):T = transaction { logger.addLogger(ExposedSqlLogger); f() }

    object ExposedSqlLogger : SqlLogger {
        private val log = Logger.getLogger(this)
        override fun log (context: StatementContext, transaction: Transaction) = log.debug(context.expandArgs(transaction))
    }
}
