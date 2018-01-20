package com.lavans.kotmi

import org.junit.Assert.assertEquals
import org.junit.Test
import com.lavans.kotmi.DataSource.DatabaseConfig

class DatabaseConfigTest {
    private val log = Logger.getLogger(this)
    @Test
    fun readConfig(): Unit {
        val config = DatabaseConfig.load()
        log.info(config.toString())
        assertEquals("jdbc:h2:mem:regular", config.url)
        assertEquals("org.h2.Driver", config.driver)
        assertEquals("user", config.user)
        assertEquals("pass", config.pass)
    }

}