package com.dannbrown.deltaboxlib.registrate.registry

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import dev.architectury.platform.Platform
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Supplier

class ConfigRegistry(private val modId: String) {
  private val configFile: File = File(Platform.getConfigFolder().toFile(), "$modId.conf")
  private val properties = mutableMapOf<String, ConfigProperty<*>>()
  private var frozen = false

  // Register properties
  fun registerBoolean(key: String, defaultValue: Boolean, comment: String?) {
    if (frozen) return
    properties[key] = BooleanConfigProperty(key, defaultValue, comment)
  }

  fun registerInt(key: String, defaultValue: Int, comment: String?) {
    if (frozen) return
    properties[key] = IntConfigProperty(key, defaultValue, comment)
  }

  fun registerFloat(key: String, defaultValue: Float, comment: String?) {
    if (frozen) return
    properties[key] = FloatConfigProperty(key, defaultValue, comment)
  }

  fun registerString(key: String, defaultValue: String, comment: String?) {
    if (frozen) return
    properties[key] = StringConfigProperty(key, defaultValue, comment)
  }

  // Load or create the config file
  fun loadConfig() {
    if (!configFile.exists()) {
      createConfigFile()
    }

    val jsonContent = try {
      FileReader(configFile).use { it.readText() }
    } catch (e: IOException) {
      e.printStackTrace()
      return
    }

    val json = JsonParser.parseString(jsonContent).asJsonObject

    properties.forEach { (key, property) ->
      val jsonElement = json.get(key)
      if (jsonElement != null) {
        property.loadValue(jsonElement)
      } else {
        property.saveValue()
      }
    }

    // Ensure all registered properties are saved
    saveConfig()
  }

  // Save the config file
  private fun saveConfig() {
    val jsonObject = mutableMapOf<String, JsonElement>()
    properties.forEach { (key, property) ->
      jsonObject[key] = property.toJson()
    }

    try {
      FileWriter(configFile).use { writer ->
        writer.write("{\n")
        jsonObject.forEach { (key, value) ->
          writer.write("  \"$key\": ${value.toString()},\n")
        }
        writer.write("}\n")
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
    abstract fun loadValue(jsonElement: JsonElement)
    abstract fun saveValue()

    abstract fun toJson(): JsonElement
  }

  private class BooleanConfigProperty(key: String, defaultValue: Boolean, comment: String?) :
    ConfigProperty<Boolean>(key, defaultValue, comment) {

    private var value: Boolean = defaultValue

    override fun loadValue(jsonElement: JsonElement) {
      value = jsonElement.asBoolean
    }

    override fun saveValue() {
      // Code to save boolean value to the config
    }

    override fun toJson(): JsonElement {
      return JsonPrimitive(value)
    }
  }

  private class IntConfigProperty(key: String, defaultValue: Int, comment: String?) :
    ConfigProperty<Int>(key, defaultValue, comment) {

    private var value: Int = defaultValue

    override fun loadValue(jsonElement: JsonElement) {
      value = jsonElement.asInt
    }

    override fun saveValue() {
      // Code to save integer value to the config
    }

    override fun toJson(): JsonElement {
      return JsonPrimitive(value)
    }
  }

  private class FloatConfigProperty(key: String, defaultValue: Float, comment: String?) :
    ConfigProperty<Float>(key, defaultValue, comment) {

    private var value: Float = defaultValue

    override fun loadValue(jsonElement: JsonElement) {
      value = jsonElement.asFloat
    }

    override fun saveValue() {
      // Code to save float value to the config
    }

    override fun toJson(): JsonElement {
      return JsonPrimitive(value)
    }
  }

  private class StringConfigProperty(key: String, defaultValue: String, comment: String?) :
    ConfigProperty<String>(key, defaultValue, comment) {

    private var value: String = defaultValue

    override fun loadValue(jsonElement: JsonElement) {
      value = jsonElement.asString
    }

    override fun saveValue() {
      // Code to save string value to the config
    }

    override fun toJson(): JsonElement {
      return JsonPrimitive(value)
    }
  }
}
