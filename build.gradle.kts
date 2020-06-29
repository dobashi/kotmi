import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    maven
    kotlin("jvm") version "1.3.61"
}

group = "com.lavans.kotmi"
version = "1.0"


buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.10"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }

}

val kotlin_version: String by extra

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = URI("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = URI("https://jitpack.io") }
    maven{ url = URI("https://dl.bintray.com/kotlin/exposed/") }
}

val exposed_version = "0.9.1"

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation(kotlin("reflect", kotlin_version))
    implementation("org.jetbrains.exposed:exposed:$exposed_version") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("com.zaxxer:HikariCP:2.7.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testImplementation("com.h2database:h2:1.4.196")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

