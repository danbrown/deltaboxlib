package com.dannbrown.databoxlib.init

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.item.FuelItem
import com.dannbrown.databoxlib.content.item.GelBubbleItem
import com.dannbrown.databoxlib.datagen.content.ItemGen
import com.dannbrown.databoxlib.lib.LibData
import com.dannbrown.databoxlib.lib.LibTags
import com.simibubi.create.AllItems
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ProjectItems {
  // @ Registry
  val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectContent.MOD_ID)
  fun register(bus: IEventBus?) {
    ITEMS.register(bus)
  }


  //  val STARFLEET_CHESPLATE = ItemGen.chestplateItem("starfleet", ArmorMaterials.LEATHER, { Items.CLAY }, "_chestplate")
  //  val STARFLEET_LEGGINGS = ItemGen.leggingsItem("starfleet", ArmorMaterials.LEATHER, { Items.CLAY }, "_leggings")
//  val STARFLEET_BOOTS = ItemGen.bootsItem("starfleet", ArmorMaterials.LEATHER, { Items.CLAY }, "_boots")
  // @ Items
//  // adamantium
//  val ADAMANTIUM_INGOT = ItemGen.ingotItem(LibData.NAMES.ADAMANTIUM, { p -> Item(p) })
//  val ADAMANTIUM_FRAGMENT = ItemGen.simpleItem(LibData.ITEMS.ADAMANTIUM_FRAGMENT, { p -> Item(p) }, LibTags.forgeItemTag("raw_materials/adamantium"))
//  // bronze
//  val BRONZE_INGOT = ItemGen.ingotItem(LibData.NAMES.BRONZE, { p -> Item(p) })
//  val BRONZE_NUGGET = ItemGen.nuggetItem(LibData.NAMES.BRONZE, { BRONZE_INGOT.get() }, { p -> Item(p) })
  // tech
  val CATHODE_TUBE = ItemGen.simpleItem(LibData.ITEMS.CATHODE_TUBE, { p -> Item(p) })

  // rubber
  val CONDENSED_LATEX = ItemGen.simpleItem(LibData.ITEMS.CONDENSED_LATEX, { p -> Item(p) })
  val RUBBER = ItemGen.simpleItem(LibData.ITEMS.RUBBER, { p -> Item(p) }, LibTags.forgeItemTag("rubber"), LibTags.forgeItemTag("ingots/rubber"))

  // steel
  val COAL_COKE = ItemGen.ingotItemControlled(LibData.ITEMS.RAW_CARBON, { LibTags.forgeItemTag("ingots/coal_coke") }, { p -> FuelItem(p, 3200) })
  val CARBON_GRIT = ItemGen.dustItemControlled(LibData.ITEMS.CARBON_GRIT, { LibTags.forgeItemTag("dusts/carbon") }, { p -> Item(p) })
  val STEEL_BLEND = ItemGen.dustItemControlled(LibData.ITEMS.STEEL_BLEND, { LibTags.forgeItemTag("dusts/steel") }, { p -> Item(p) })
  val STEEL_INGOT = ItemGen.ingotItem(LibData.NAMES.STEEL, { p -> Item(p) })
  val STEEL_NUGGET = ItemGen.nuggetItem(LibData.NAMES.STEEL, { STEEL_INGOT.get() }, { p -> Item(p) })
  val STEEL_SHEET = ItemGen.sheetItem(LibData.NAMES.STEEL, { p -> Item(p) })

  // tungsten
  val RAW_TUNGSTEN = ItemGen.rawOreItem(LibData.NAMES.TUNGSTEN, { p -> Item(p) })
  val CRUSHED_RAW_TUNGSTEN = ItemGen.crushedRawOreItem(LibData.NAMES.TUNGSTEN, { p -> Item(p) })
  val TUNGSTEN_INGOT = ItemGen.ingotItem(LibData.NAMES.TUNGSTEN, { p -> Item(p) })
  val TUNGSTEN_NUGGET = ItemGen.nuggetItem(LibData.NAMES.TUNGSTEN, { TUNGSTEN_INGOT.get() }, { p -> Item(p) })

  // titanium
  val TITANIUM_INGOT = ItemGen.ingotItem(LibData.NAMES.TITANIUM, { p -> Item(p) })
  val TITANIUM_NUGGET = ItemGen.nuggetItem(LibData.NAMES.TITANIUM, { TITANIUM_INGOT.get() }, { p -> Item(p) })

  //  // tin
//  val RAW_TIN = ItemGen.rawOreItem(LibData.NAMES.TIN, { p -> Item(p) })
//  val CRUSHED_RAW_TIN = ItemGen.crushedRawOreItem(LibData.NAMES.TIN, { p -> Item(p) })
//  val TIN_INGOT = ItemGen.ingotItem(LibData.NAMES.TIN, { p -> Item(p) })
//  val TIN_NUGGET = ItemGen.nuggetItem(LibData.NAMES.TIN, { TIN_INGOT.get() }, { p -> Item(p) })
  // aluminium
  val RAW_ALUMINIUM = ItemGen.rawOreItem(LibData.NAMES.ALUMINIUM, { p -> Item(p) })
  val CRUSHED_RAW_ALUMINIUM = ItemGen.crushedRawOreItem(LibData.NAMES.ALUMINIUM, { p -> Item(p) })
  val ALUMINIUM_INGOT = ItemGen.ingotItem(LibData.NAMES.ALUMINIUM, { p -> Item(p) })
  val ALUMINIUM_NUGGET = ItemGen.nuggetItem(LibData.NAMES.ALUMINIUM, { ALUMINIUM_INGOT.get() }, { p -> Item(p) })
  val ALUMINIUM_SHEET = ItemGen.sheetItem(LibData.NAMES.ALUMINIUM, { p -> Item(p) })

  // silica
  val SILICA_CRYSTAL = ItemGen.ingotItemControlled(LibData.ITEMS.SILICA_CRYSTAL, { LibTags.forgeItemTag("ingots/silica") }, { p -> Item(p) })
  val SILICA_GRAINS = ItemGen.nuggetItemControlled(LibData.ITEMS.SILICA_GRAINS, { LibTags.forgeItemTag("nuggets/silica") }, { DataIngredient.tag(LibTags.forgeItemTag("ingots/silica")) }, { SILICA_CRYSTAL.get() }, { p -> Item(p) })
  val REFINED_SILICON = ItemGen.ingotItemControlled(LibData.ITEMS.REFINED_SILICON, { LibTags.forgeItemTag("ingots/silicon") }, { p -> Item(p) })

  // others
  val RAW_GRAPHITE = ItemGen.ingotItemControlled(LibData.ITEMS.RAW_GRAPHITE, { LibTags.forgeItemTag("ingots/graphite") }, { p -> Item(p) })

  // gems
  val PHOSPHORUS_POWDER = ItemGen.dustItemControlled(LibData.ITEMS.PHOSPHORUS_POWDER, { LibTags.forgeItemTag("dusts/phosphorus") }, { p -> Item(p) })
  val SULPHUR = ItemGen.dustItemControlled(LibData.ITEMS.SULPHUR, { LibTags.forgeItemTag("dusts/sulphur") }, { p -> Item(p) })
  val MAGNESIUM_DUST = ItemGen.dustItemControlled(LibData.ITEMS.MAGNESIUM_DUST, { LibTags.forgeItemTag("dusts/magnesium") }, { p -> Item(p) })
  val HIMALAYAN_SALT = ItemGen.dustItemControlled(LibData.ITEMS.HIMALAYAN_SALT, { LibTags.forgeItemTag("ingots/himalayan_salt") }, { p -> Item(p) })
  val SALT = ItemGen.dustItemControlled(LibData.ITEMS.SALT, { LibTags.forgeItemTag("dusts/salt") }, { p -> Item(p) })
  val ROSE_QUARTZ_SHARD = ItemGen.nuggetItemControlled(LibData.ITEMS.ROSE_QUARTZ_SHARD, { LibTags.forgeItemTag("nuggets/rose_quartz") }, { DataIngredient.items(AllItems.ROSE_QUARTZ.get()) }, { AllItems.ROSE_QUARTZ.get() }, { p -> Item(p) })
  val PERIDOT = ItemGen.gemItem(LibData.ITEMS.PERIDOT, { p -> Item(p) })

  // utils
  val WATER_GEL_BUBBLE = ItemGen.simpleItem(LibData.ITEMS.WATER_GEL_BUBBLE, { p -> GelBubbleItem({ Blocks.WATER }, p.stacksTo(8)) })
  val LAVA_GEL_BUBBLE = ItemGen.simpleItem(LibData.ITEMS.LAVA_GEL_BUBBLE, { p -> GelBubbleItem({ Blocks.LAVA }, p.stacksTo(8)) })

  // PLANTS
  val COTTON_SEEDS = ItemGen.simpleItem(LibData.ITEMS.COTTON_SEEDS, { p -> ItemNameBlockItem(ProjectBlocks.BUDDING_COTTON_CROP.get(), p) })
  val COTTON_PULP = ItemGen.simpleItem(LibData.ITEMS.COTTON_PULP, { p -> FuelItem(p, 400) })

  // TOOLS
  val ADAMANTIUM_PICKAXE = ItemGen.simpleItem(LibData.NAMES.ADAMANTIUM + "_pickaxe", { p -> PickaxeItem(ProjectToolTiers.TUNGSTEN, 2, -3f, p) }, LibTags.forgeItemTag("tools/adamantium"), LibTags.databoxlibItemTag("adamanitum_tools"))
  val ADAMANTIUM_AXE = ItemGen.simpleItem(LibData.NAMES.ADAMANTIUM + "_axe", { p -> AxeItem(ProjectToolTiers.TUNGSTEN, 8f, -3f, p) }, LibTags.forgeItemTag("tools/adamantium"), LibTags.databoxlibItemTag("adamanitum_tools"))
  val ADAMANTIUM_SHOVEL = ItemGen.simpleItem(LibData.NAMES.ADAMANTIUM + "_shovel", { p -> ShovelItem(ProjectToolTiers.TUNGSTEN, 2.5f, -3f, p) }, LibTags.forgeItemTag("tools/adamantium"), LibTags.databoxlibItemTag("adamanitum_tools"))
  val ADAMANTIUM_SWORD = ItemGen.simpleItem(LibData.NAMES.ADAMANTIUM + "_sword", { p -> SwordItem(ProjectToolTiers.TUNGSTEN, 3, -2.4f, p) }, LibTags.forgeItemTag("tools/adamantium"), LibTags.databoxlibItemTag("adamanitum_tools"))

  // ARMORS
  val STEEL_SPACE_HELMET = ItemGen.helmetItem(LibData.NAMES.STEEL, { ProjectArmorMaterials.STEEL }, { STEEL_INGOT.get() }, "_space_helmet")
  val STEEL_BACKTANK = ItemGen.backtankItem(LibData.NAMES.STEEL, { ProjectArmorMaterials.STEEL }, { ProjectBlocks.STEEL_BACKTANK.get() }, { STEEL_INGOT.get() }, { STEEL_SHEET.get() })
  val STEEL_SPACE_LEGGINGS = ItemGen.leggingsItem(LibData.NAMES.STEEL, { ProjectArmorMaterials.STEEL }, { STEEL_INGOT.get() }, "_space_leggings")
  val STEEL_SPACE_BOOTS = ItemGen.bootsItem(LibData.NAMES.STEEL, { ProjectArmorMaterials.STEEL }, { STEEL_INGOT.get() }, "_space_boots")

  // @ Blacklist for creative tab
//  val DONT_INCLUDE_CREATIVE: MutableList<RegistryObject<Item>> = ArrayList()
}
