package com.dannbrown.deltaboxlib.registrate.builders

import com.dannbrown.deltaboxlib.registrate.AbstractDeltaboxRegistrate
import net.minecraft.tags.TagKey
import java.util.function.Supplier

open class TagBuilder<T>(
  val registrate: AbstractDeltaboxRegistrate, val hostTag: TagKey<T>
) {
  private val entries: MutableList<Supplier<out T>> = mutableListOf()
  private val childTags: MutableList<TagKey<T>> = mutableListOf()

  fun add(entry: Supplier<out T>): TagBuilder<T> {
    entries.add(entry)
    return this
  }

  fun add(childTag: TagKey<T>): TagBuilder<T> {
    childTags.add(childTag)
    return this
  }

  fun getTagEntries(): MutableList<Supplier<out T>> {
    return entries
  }

  fun getTagChilds(): MutableList<TagKey<T>> {
    return childTags
  }

  open fun register() {
    throw IllegalArgumentException("Unsupported tag type")
  }
}