package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import java.util.function.Supplier

open class TagBuilder<T>(
  val registrate: AbstractDeltaboxRegistrate, val hostTag: TagKey<T>
) {
  private val entries: MutableList<Supplier<out T>> = mutableListOf()
  private val childTags: MutableList<TagKey<T>> = mutableListOf()
  private val keys: MutableList<ResourceKey<T>> = mutableListOf()

  fun add(vararg entry: Supplier<out T>): TagBuilder<T> {
    entries.addAll(entry)
    return this
  }

  fun add(vararg childTag: TagKey<T>): TagBuilder<T> {
    childTags.addAll(childTag)
    return this
  }

  fun add(vararg key: ResourceKey<T>): TagBuilder<T> {
    keys.addAll(key)
    return this
  }

  fun getTagEntries(): MutableList<Supplier<out T>> {
    return entries
  }

  fun getTagChilds(): MutableList<TagKey<T>> {
    return childTags
  }

  fun getTagKeys(): MutableList<ResourceKey<T>> {
    return keys
  }

  open fun register() {
    throw IllegalArgumentException("Unsupported tag type")
  }
}