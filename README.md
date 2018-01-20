# Kotmi

### Migration tool for kotlin.

## Sample

```kotlin:Sample.kt
import com.lavans.kotmi.DataSource.DatabaseConfig
import com.lavans.kotmi.Kotmi.Migration
...

DataSource.connect(DatabaseConfig.load())
// Make some migration functions with version id.
val m1 = Migration("0.0.1", {
    SchemaUtils.create(MigrationTestTable)
    SchemaUtils.createIndex(arrayOf(MigrationTestTable.id), true)
})
val m2 = Migration("0.0.2", {
    SchemaUtils.createIndex(arrayOf(MigrationTestTable.value), true)
})
// Call 'Kotmi.migrate()' with module id
Kotmi.migrate("module_id", m1, m2)
// That's all ;)
```

Executable sample.
[KotmiTest.kt](https://github.com/dobashi/kotmi/blob/master/src/test/kotlin/com/lavans/kotmi/KotmiTest.kt)

## Prepare

This project is not deployed to maven central yet.

```bash
$ git clone https://github.com/dobashi/kotmi.git
$ cd kotmi
$ ./gradlew install
```


Add dependency to your project.

gradle Kotlin DSL sample
```kotlin:build.gradle.kts
dependencies {
    compile("com.lavans.kotmi:kotmi:1.0")
}
```

Make database.json with your database configuration.
```json
{
  "url": "jdbc:h2:mem:regular",
  "driver": "org.h2.Driver",
  "user":"user",
  "pass":"pass"
}

```

## TODO

* Configure version management table name.
* Configure logging level.
* Without DatabaseConfig/ConnectionPool version.

## FAQ

### What is the name of version management table?

kotomi_management_versions

### Does Kotmi support down version?

No. Please make reverse migration for downgrading.

### Does Kotmi support SQL migration?

No. Kotmi support only `()->Unit` funciton. [KotminTest](https://github.com/dobashi/kotmi/blob/master/src/test/kotlin/com/lavans/kotmi/KotmiTest.kt) uses [Exposed](https://github.com/JetBrains/Exposed) to migrate Database schema.


<!--
### What is the difference between 'Kotmi' and 'Kotmi-ds'?

'Kotmi-ds' is 'Kotmi with DataSource'. This depends on 'HikariCP' and has some database configuration utilities.
-->

