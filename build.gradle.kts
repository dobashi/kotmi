import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "com.lavans.kotmi"
version = "1.0"


buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.10"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }

}

apply {
    plugin("kotlin")
    plugin("maven")
}

val kotlin_version: String by extra

repositories {
//    mavenLocal()
    mavenCentral()
    maven{
        url = URI("https://dl.bintray.com/kotlin/exposed/")
    }
}

val exposed_version = "0.9.1"

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile(kotlinModule("reflect", kotlin_version))
    compile("org.jetbrains.exposed:exposed:$exposed_version") {
        exclude(group = "org.jetbrains.kotlin")
    }
    compile("com.zaxxer:HikariCP:2.7.4")
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.3")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testCompile("com.h2database:h2:1.4.196")
    testCompile("ch.qos.logback:logback-classic:1.2.3")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

