package com.lavans.kotmi

import com.lavans.kotmi.DataSource.transactionWithLog
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

/** https://github.com/dobashi/kotmi */
object Kotmi {
    private val log = Logger.getLogger(this)

    /** Create table if it doesn't exist. */
    init {
        transactionWithLog {
            if (!Versions.exists()) {
                log.info("Kotmi version management start")
                SchemaUtils.create(Versions)
            }
        }
    }

    /** Execute migration functions.
     * @return List of executed versions.
     */
    fun migrate(id: ModuleID, vararg migrations: Migration): List<VersionID> {
        val currentVersion: String? = transactionWithLog {
            current(id)
        }
        return migrations.filter { it.version > currentVersion ?: "" }.map {
            transactionWithLog {
                it.function()
                updateOrInsert(id, it)
                it.version
            }
        }
    }

    /** Get current version. */
    private fun current(id: ModuleID): VersionID? = Versions
        .select { Versions.id.eq(id) }
        .firstOrNull()?.get(Versions.version)

    /** * Update version. If update result is 0 then insert. */
    private fun updateOrInsert(moduleID: ModuleID, migration: Migration) {
        fun update(moduleID: ModuleID, migration: Migration): Boolean {
            return Versions.update({
                Versions.id.eq(moduleID)
            }) {
                it[version] = migration.version
                it[updatedAt] = DateTime.now()
            } == 1
        }

        fun insert(moduleID: ModuleID, migration: Migration)  {
            Versions.insert {
                it[id] = EntityID(moduleID, Versions)
                it[version] = migration.version
                it[createdAt] = DateTime.now()
                it[updatedAt] = DateTime.now()
            }
        }
        if (update(moduleID, migration)) else insert(moduleID, migration)
    }

    /** Clear version information.
     * @return true: deleted, false: not deleted */
    fun clear(id: ModuleID): Boolean =
        transactionWithLog { Versions.deleteWhere { Versions.id.eq(EntityID(id, Versions)) } } == 1

    data class Migration(
        val version: VersionID,
        inline val function: () -> Unit
    )

    /** Kotmi management table */
    object Versions : IdTable<String>("kotomi_management_versions") {
        override val id: Column<EntityID<String>> = varchar("id", 100).primaryKey().entityId()
        val version: Column<String> = varchar("version", 100)
        val createdAt = datetime("created_at")
        val updatedAt = datetime("updated_at")
    }
}






