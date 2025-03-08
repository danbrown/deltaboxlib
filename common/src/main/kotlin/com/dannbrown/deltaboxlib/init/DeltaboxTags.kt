package com.dannbrown.deltaboxlib.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil

object DeltaboxTags {
  object ITEM {
    val EXCLUDE_FROM_CREATIVE = DeltaboxUtil.TAGS.deltaboxItemTag("exclude_from_creative")
  }

  fun register() {
    // init class
  }
}