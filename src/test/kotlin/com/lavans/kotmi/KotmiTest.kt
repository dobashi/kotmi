package com.lavans.kotmi

import com.lavans.kotmi.DataSource.DatabaseConfig
import com.lavans.kotmi.Kotmi.Migration
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class KotmiTest {
    @Before
    fun before() {
        DataSource.connect()
    }

    @Test
    fun migrate() {
        val mid: ModuleID = "kotmi_test"
        // do 2 migrations
        assertEquals(2, Kotmi.migrate(mid, m1, m2).count())
        // only 1 migration should be executed
        assertEquals(1, Kotmi.migrate(mid, m1, m2, m3).count())
        // do nothing
        assertEquals(0, Kotmi.migrate(mid, m3).count())
        // clear successed
        assertTrue(Kotmi.clear(mid))

    }
    private val m1 = Migration("0.0.1", {
        SchemaUtils.create(MigrationTestTable)
        SchemaUtils.createIndex(arrayOf(MigrationTestTable.id), true)
    })
    private val m2 = Migration("0.0.2", {
        SchemaUtils.createIndex(arrayOf(MigrationTestTable.value), true)
    })

    private val m3 = Migration("0.0.3", {
        SchemaUtils.drop(MigrationTestTable)
    })

    object MigrationTestTable : Table("migration_test_tmp") {
        val id = varchar("id", 100)
        val value = varchar("value", 100)
    }
}

