package com.dannbrown.deltaboxlib.sample.datagen.lang

import com.dannbrown.deltaboxlib.DeltaboxLib

object SampleLangGen {
  fun addStaticLangs(doRun: Boolean) {
    if (!doRun) return // avoid running in the server-side

    DeltaboxLib.REGISTRATE.addGenericTooltipLang("flint", "It's a Delta!")
  }
}