package com.dannbrown.deltaboxlib.registrate.registry

import dev.architectury.platform.Platform
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class ConfigRegistry(private val modId: String) {
  private val configFile: File = File(Platform.getConfigFolder().toFile(), "$modId.common.conf")
  private val properties = mutableMapOf<String, ConfigProperty<*>>()
  private var frozen = false

  // Register properties and return a ConfigSupplier that provides the value lazily after the config is frozen
  fun registerBoolean(key: String, defaultValue: Boolean, comment: String?): ConfigSupplier<Boolean> {
    if (frozen) return ConfigSupplier { (properties[key] as BooleanConfigProperty).getValue() }
    val property = BooleanConfigProperty(key, defaultValue, comment)
    properties[key] = property
    return ConfigSupplier { property.getValue() }
  }

  fun registerInt(key: String, defaultValue: Int, comment: String?): ConfigSupplier<Int> {
    if (frozen) return ConfigSupplier { (properties[key] as IntConfigProperty).getValue() }
    val property = IntConfigProperty(key, defaultValue, comment)
    properties[key] = property
    return ConfigSupplier { property.getValue() }
  }

  fun registerFloat(key: String, defaultValue: Float, comment: String?): ConfigSupplier<Float> {
    if (frozen) return ConfigSupplier { (properties[key] as FloatConfigProperty).getValue() }
    val property = FloatConfigProperty(key, defaultValue, comment)
    properties[key] = property
    return ConfigSupplier { property.getValue() }
  }

  fun registerString(key: String, defaultValue: String, comment: String?): ConfigSupplier<String> {
    if (frozen) return ConfigSupplier { (properties[key] as StringConfigProperty).getValue() }
    val property = StringConfigProperty(key, defaultValue, comment)
    properties[key] = property
    return ConfigSupplier { property.getValue() }
  }

  // Load or create the config file
  fun loadConfig() {
    if (!configFile.exists()) {
      createConfigFile()  // Create the config file if it doesn't exist
    }

    val lines = try {
      FileReader(configFile).use { it.readLines() }
    } catch (e: IOException) {
      e.printStackTrace()
      return
    }

    // Parse the lines to load values
    var insideCommentBlock = false
    lines.forEach { line ->
      if (line.trim().startsWith("#")) {
        // It's a comment, skip or process accordingly
      } else if (line.contains("=")) {
        val (key, value) = line.split("=", limit = 2).map { it.trim() }
        val property = properties[key]
        property?.loadValue(value)
      }
    }

    // Save the config after loading
    saveConfig()

    frozen = true  // Mark config as frozen after loading
  }

  // Save the config file
  private fun saveConfig() {
    try {
      FileWriter(configFile).use { writer ->
        writer.write("# Config for $modId\n")
        properties.forEach { (key, property) ->
          writer.write("${property.comment?.let { "# $it" } ?: ""}\n")
          writer.write("$key=${property.toStringValue()}\n")
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  // Create an empty config file with comments
  private fun createConfigFile() {
    try {
      Files.createDirectories(Paths.get(configFile.parent))
      configFile.createNewFile()
      val writer = FileWriter(configFile)
      writer.write("# Config for $modId\n")
      writer.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  // Freeze config, preventing any further modifications
  fun freeze() {
    frozen = true
  }

  // Clear the config
  fun clear() {
    properties.clear()
  }

  private abstract class ConfigProperty<T>(val key: String, val defaultValue: T, val comment: String?) {
    abstract fun loadValue(value: String)
    abstract fun toStringValue(): String
    abstract fun getValue(): T
  }

  private class BooleanConfigProperty(key: String, defaultValue: Boolean, comment: String?) :
    ConfigProperty<Boolean>(key, defaultValue, comment) {

    private var value: Boolean = defaultValue

    override fun loadValue(value: String) {
      this.value = value.toBoolean()
    }

    override fun toStringValue(): String {
      return value.toString()
    }

    override fun getValue(): Boolean = value
  }

  private class IntConfigProperty(key: String, defaultValue: Int, comment: String?) :
    ConfigProperty<Int>(key, defaultValue, comment) {

    private var value: Int = defaultValue

    override fun loadValue(value: String) {
      this.value = value.toInt()
    }

    override fun toStringValue(): String {
      return value.toString()
    }

    override fun getValue(): Int = value
  }

  private class FloatConfigProperty(key: String, defaultValue: Float, comment: String?) :
    ConfigProperty<Float>(key, defaultValue, comment) {

    private var value: Float = defaultValue

    override fun loadValue(value: String) {
      this.value = value.toFloat()
    }

    override fun toStringValue(): String {
      return value.toString()
    }

    override fun getValue(): Float = value
  }

  private class StringConfigProperty(key: String, defaultValue: String, comment: String?) :
    ConfigProperty<String>(key, defaultValue, comment) {

    private var value: String = defaultValue

    override fun loadValue(value: String) {
      this.value = value
    }

    override fun toStringValue(): String {
      return value
    }

    override fun getValue(): String = value
  }

  // ConfigSupplier to provide a lazy getter after the config is frozen
  class ConfigSupplier<T>(private val valueGetter: () -> T) {
    private var value: T? = null

    fun get(): T {
      if (value == null) {
        value = valueGetter()
      }
      return value!!
    }
  }
}
