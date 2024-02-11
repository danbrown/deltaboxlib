package com.dannbrown.databoxlib.datagen.tags



import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.init.ProjectItems
import com.dannbrown.databoxlib.init.ProjectTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ProjectItemTags(
  output: PackOutput,
  future: CompletableFuture<HolderLookup.Provider>,
  provider: CompletableFuture<TagLookup<Block>>,
  fileHelper: ExistingFileHelper
) :
  ItemTagsProvider(output, future, provider, ProjectContent.MOD_ID, fileHelper) {
  override fun getName(): String {
    return "Databox Item Tags"
  }

  override public fun tag(tagKey: TagKey<Item>): IntrinsicTagAppender<Item> {
    return super.tag(tagKey)
  }

  override fun addTags(provider: HolderLookup.Provider) {
    // BEWARE: It can replace tags created by the REGISTRATE system

    tag(ProjectTags.ITEM.RUBBER_REPLACEMENTS).add(
      Items.DRIED_KELP,
      ProjectItems.RUBBER.get()
    )

//    tag(DatagenTags.Items.RAW_MATERIALS_ADADMANTIUM).add(DatagenItems.ADAMANTIUM_FRAGMENT.get())
  }
}