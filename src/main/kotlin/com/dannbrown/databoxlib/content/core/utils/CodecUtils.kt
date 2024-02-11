package com.dannbrown.databoxlib.content.core.utils

import com.mojang.serialization.Codec

object CodecUtils {
  fun <T : Enum<T>> createEnumCodec(enumClass: Class<T>): Codec<T> {
    return Codec.STRING.xmap({ s: String ->
      enumClass.getEnumConstants()
        .firstOrNull { it.name.equals(s, ignoreCase = true) } ?: throw IllegalArgumentException("Unknown enum value $s for enum class $enumClass")
    }
    ) { obj: Enum<*> -> obj.name }
  }
}