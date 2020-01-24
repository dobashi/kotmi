package com.lavans.kotmi

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream

typealias ModuleID = String
typealias VersionID = String

object Logger {
    fun getLogger(v: String): Logger = LoggerFactory.getLogger(v)
    fun getLogger(o: Any): Logger = getLogger(o.javaClass.canonicalName)
}

class JsonObjectMapper {
    companion object {
        private val om = ObjectMapper().registerKotlinModule().enable(JsonParser.Feature.ALLOW_COMMENTS)
        fun mapper(): ObjectMapper = om
        fun <T>read(src: InputStream, clazz: Class<T>): T = om.readValue(src, clazz)
        fun asString(v: Any):String = om.writeValueAsString(v)
    }
}

object File {
    fun resourceAsStream(filename: String): java.io.InputStream =
        javaClass.classLoader.getResourceAsStream(filename)

    fun resourceAsString(filename: String): String =
        resourceAsStream(filename).use { it.bufferedReader().readText() }
}

object DateTime {
    fun now(): DateTime = DateTime.now()
}
