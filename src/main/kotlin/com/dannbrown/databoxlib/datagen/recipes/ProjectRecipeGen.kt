package com.dannbrown.databoxlib.datagen.recipes

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.recipe.CoolingRecipe
import com.dannbrown.databoxlib.content.recipe.ElectrolyzerRecipe
import com.dannbrown.databoxlib.content.recipe.PressureChamberRecipe
import com.dannbrown.databoxlib.init.ProjectRecipeTypes
import com.dannbrown.databoxlib.lib.LibTags
import com.dannbrown.databoxlib.lib.LibUtils
import com.simibubi.create.*
import com.simibubi.create.content.fluids.transfer.FillingRecipe
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe
import com.simibubi.create.content.kinetics.millstone.MillingRecipe
import com.simibubi.create.content.kinetics.press.PressingRecipe
import com.simibubi.create.content.kinetics.saw.CuttingRecipe
import com.simibubi.create.content.processing.basin.BasinRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider.GeneratedRecipe
import com.simibubi.create.foundation.utility.RegisteredObjects
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCookingSerializer
import net.minecraft.world.level.ItemLike
import net.minecraftforge.fluids.FluidType
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.UnaryOperator
import kotlin.reflect.KClass

// class based on https://github.com/Creators-of-Create/Create/blob/mc1.19/0.5.1/src/main/java/com/simibubi/create/foundation/data/recipe/ProcessingRecipeGen.java
// the goal is to be able to create custom processing recipes
abstract class ProjectRecipeGen(generator: DataGenerator) : CreateRecipeProvider(generator.packOutput) {
  abstract val recipeName: String
  override fun buildRecipes(consumer: Consumer<FinishedRecipe>) {
    all.forEach(Consumer { c: GeneratedRecipe -> c.register(consumer) })
    ProjectContent.LOGGER.info(recipeName + " registered " + all.size + " recipe" + if (all.size == 1) "" else "s")
  }

  companion object {
    val GENERATORS: MutableList<KClass<out ProjectRecipeGen>> = ArrayList()
    fun registerGenerator(generator: KClass<out ProjectRecipeGen>) {
      GENERATORS.add(generator)
    }

    init {
      // combo recipes
      registerGenerator(CasingsRecipeGen::class)
      registerGenerator(RenewablesRecipeGen::class)
      registerGenerator(OresRecipeGen::class)
      registerGenerator(WoodRecipeGen::class)
      // standard recipes
      registerGenerator(StandardRecipeGen::class)
      registerGenerator(CookingRecipeGen::class)
      // create recipes
      registerGenerator(CompactingRecipeGen::class)
      registerGenerator(PressingRecipeGen::class)
      registerGenerator(MixingRecipeGen::class)
      registerGenerator(SplashingRecipeGen::class)
      registerGenerator(FillingRecipeGen::class)
      registerGenerator(MillingCrushingRecipeGen::class)
      // databoxlib recipes
      registerGenerator(ElectrolysisRecipeGen::class)
      registerGenerator(PressureChamberRecipeGen::class)
      registerGenerator(CoolingRecipeGen::class)
      registerGenerator(PlaceholderRecipeGen::class)
    }

    protected val BUCKET = FluidType.BUCKET_VOLUME
    protected val BOTTLE = 250
    fun createSimpleLocation(
      recipeType: String,
      result: Supplier<ItemLike>,
      prefix: String,
      suffix: String
    ): ResourceLocation {
      return LibUtils.resourceLocation(
        recipeType + "/" + prefix + RegisteredObjects.getKeyOrThrow(
          result.get()
            .asItem()
        ).path + suffix
      )
    }

    fun registerSingle(doRun: Boolean, gen: DataGenerator, generator: KClass<out ProjectRecipeGen>) {
      gen.addProvider(doRun, object : DataProvider {
        override fun getName(): String {
          return "Databox's Processing Recipes"
        }

        @Throws(IOException::class)
        override fun run(dc: CachedOutput): CompletableFuture<*> {
          try {
            val generatorInstance = generator.java.getDeclaredConstructor(DataGenerator::class.java)
              .newInstance(gen)
            generatorInstance.run(dc)
          } catch (e: Exception) {
            e.printStackTrace()
          }

          return CompletableFuture.completedFuture(null)
        }
      })
    }

    fun registerAll(doRun: Boolean, gen: DataGenerator) {
      gen.addProvider(doRun, object : DataProvider {
        override fun getName(): String {
          return "Databox's Processing Recipes"
        }

        @Throws(IOException::class)
        override fun run(dc: CachedOutput): CompletableFuture<*> {
          try {
            for (generator in GENERATORS) {
              val generatorInstance = generator.java.getDeclaredConstructor(DataGenerator::class.java)
                .newInstance(gen)
              generatorInstance.run(dc)
            }
          } catch (e: Exception) {
            e.printStackTrace()
          }

          return CompletableFuture.completedFuture(null)
        }
      })
    }
  }

  //  override fun getName(): String {
//    return "Databox's Processing Recipes"
//  }
  // Custom generators:
  fun cooking(
    ingredient: Supplier<Ingredient>,
    result: Supplier<ItemLike>,
    builder: UnaryOperator<CookingRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allCooking = CookingRecipeBuilder(ingredient, result).apply(builder)
      .getRecipes()

    all.addAll(allCooking)
    return allCooking
  }

  fun crafting(
    result: Supplier<ItemLike>,
    amount: Int = 1,
    builder: UnaryOperator<StandardRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allCrafting = StandardRecipeBuilder(result, amount).apply(builder)
      .getRecipes()

    all.addAll(allCrafting)
    return allCrafting
  }

  fun create(
    name: String,
    builder: UnaryOperator<CreateRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allRecipes = CreateRecipeBuilder(name).apply(builder)
      .getRecipes()

    all.addAll(allRecipes)
    return allRecipes
  }

  fun databoxlib(
    name: String,
    builder: UnaryOperator<ProjectRecipeBuilder> = UnaryOperator.identity()
  ): List<GeneratedRecipe> {
    val allRecipes = ProjectRecipeBuilder(name).apply(builder)
      .getRecipes()

    all.addAll(allRecipes)
    return allRecipes
  }

  // DATABOX RECIPE CLASS
  class ProjectRecipeBuilder(name: String) {
    private var name = ""
    private var prefix: String = ""
    private var suffix: String = ""
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.name = name
    }

    fun apply(builder: UnaryOperator<ProjectRecipeBuilder>): ProjectRecipeBuilder {
      return builder.apply(this)
    }

    fun prefix(prefix: String): ProjectRecipeBuilder {
      this.prefix = prefix
      return this
    }

    fun suffix(suffix: String): ProjectRecipeBuilder {
      this.suffix = suffix
      return this
    }

    fun cooling(
      builder: UnaryOperator<ProcessingRecipeBuilder<CoolingRecipe>> = UnaryOperator.identity()
    ): ProjectRecipeBuilder {
      databoxlibRecipe(ProjectRecipeTypes.COOLING, name + "_cooling", builder)
      return this
    }

    fun electrolysis(
      builder: UnaryOperator<ProcessingRecipeBuilder<ElectrolyzerRecipe>> = UnaryOperator.identity()
    ): ProjectRecipeBuilder {
      databoxlibRecipe(ProjectRecipeTypes.ELECTROLYSIS, name + "_electrolysis", builder)
      return this
    }

    fun pressureChamber(
      builder: UnaryOperator<ProcessingRecipeBuilder<PressureChamberRecipe>> = UnaryOperator.identity()
    ): ProjectRecipeBuilder {
      databoxlibRecipe(ProjectRecipeTypes.PRESSURE_CHAMBER, name + "_pressure_chamber", builder)
      return this
    }

    // DATABOX MOD RECIPE
    protected fun <T : ProcessingRecipe<*>> databoxlibRecipe(
      recipeType: ProjectRecipeTypes,
      name: String,
      builder: UnaryOperator<ProcessingRecipeBuilder<T>>
    ): GeneratedRecipe {
      val serializer: ProcessingRecipeSerializer<T> = recipeType.getSerializer()
      val generatedRecipe =
        GeneratedRecipe { c: Consumer<FinishedRecipe> ->
          builder.apply(
            ProcessingRecipeBuilder(
              serializer.factory,
              LibUtils.resourceLocation(name)
            )
          )
            .build(c)
        }

      allRecipes.add(generatedRecipe)

      return generatedRecipe
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }

  // CREATE RECIPE CLASS
  class CreateRecipeBuilder(name: String) {
    private var name = ""
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.name = name
    }

    fun apply(builder: UnaryOperator<CreateRecipeBuilder>): CreateRecipeBuilder {
      return builder.apply(this)
    }

    // ITEM APPLICATION
    fun comboCasing(
      output: Supplier<ItemLike>,
      baseBlockTag: Supplier<Ingredient>,
      itemToApply: Supplier<Ingredient>,
    ): CreateRecipeBuilder {
      deployer(output, baseBlockTag, itemToApply)
      itemApplication(output, baseBlockTag, itemToApply)

      return this
    }

    fun deployer(
      output: Supplier<ItemLike>,
      baseBlockTag: Supplier<Ingredient>,
      itemToApply: Supplier<Ingredient>,
    ): CreateRecipeBuilder {
      createModRecipe<ItemApplicationRecipe>(AllRecipeTypes.DEPLOYING, name + "_deployer_application")
      { b ->
        b
          .require(baseBlockTag.get())
          .require(itemToApply.get())
          .output(output.get())
      }

      return this
    }

    fun itemApplication(
      output: Supplier<ItemLike>,
      baseBlockTag: Supplier<Ingredient>,
      itemToApply: Supplier<Ingredient>,
    ): CreateRecipeBuilder {
      createModRecipe<ManualApplicationRecipe>(AllRecipeTypes.ITEM_APPLICATION, name + "_manual_application") { b ->
        b
          .require(baseBlockTag.get())
          .require(itemToApply.get())
          .output(output.get())
      }

      return this
    }

    fun compacting(
      builder: UnaryOperator<ProcessingRecipeBuilder<BasinRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.COMPACTING, name + "_compacting", builder)

      return this
    }

    fun pressing(
      builder: UnaryOperator<ProcessingRecipeBuilder<PressingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.PRESSING, name + "_pressing", builder)

      return this
    }

    fun milling(
      builder: UnaryOperator<ProcessingRecipeBuilder<MillingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.MILLING, name + "_milling", builder)

      return this
    }

    fun crushing(
      builder: UnaryOperator<ProcessingRecipeBuilder<CrushingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.CRUSHING, name + "_crushing", builder)

      return this
    }

    fun mixing(
      builder: UnaryOperator<ProcessingRecipeBuilder<BasinRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.MIXING, name + "_mixing", builder)

      return this
    }

    fun cutting(
      builder: UnaryOperator<ProcessingRecipeBuilder<CuttingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.CUTTING, name + "_cutting", builder)

      return this
    }

    fun splashing(
      builder: UnaryOperator<ProcessingRecipeBuilder<SplashingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.SPLASHING, name + "_splashing", builder)

      return this
    }

    fun haunting(
      builder: UnaryOperator<ProcessingRecipeBuilder<HauntingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.HAUNTING, name + "_haunting", builder)

      return this
    }

    fun filling(
      builder: UnaryOperator<ProcessingRecipeBuilder<FillingRecipe>> = UnaryOperator.identity()
    ): CreateRecipeBuilder {
      createModRecipe(AllRecipeTypes.FILLING, name + "_filling", builder)

      return this
    }

    protected fun <T : ProcessingRecipe<*>> createModRecipe(
      recipeType: AllRecipeTypes,
      name: String,
      builder: UnaryOperator<ProcessingRecipeBuilder<T>>
    ): GeneratedRecipe {
      val serializer: ProcessingRecipeSerializer<T> = recipeType.getSerializer()
      val generatedRecipe =
        GeneratedRecipe { c: Consumer<FinishedRecipe> ->
          builder.apply(
            ProcessingRecipeBuilder(
              serializer.factory,
              LibUtils.resourceLocation(name)
            )
          )
            .build(c)
        }

      allRecipes.add(generatedRecipe)

      return generatedRecipe
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }

  // COOKING RECIPE CLASS
  class CookingRecipeBuilder(ingredient: Supplier<Ingredient>, result: Supplier<ItemLike>) {
    private var ingredient: Supplier<Ingredient>? = null
    private var result: Supplier<ItemLike>? = null
    private var unlockedBy: Supplier<ItemPredicate>? = null
    private var prefix: String = ""
    private var suffix: String = ""
    private var exp: Float = 0f
    private var cookingTime: Int = 200
    private val COOKING_TIME_MODIFIER = 1.0f
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.ingredient = ingredient
      this.result = result
    }

    fun apply(builder: UnaryOperator<CookingRecipeBuilder>): CookingRecipeBuilder {
      return builder.apply(this)
    }

    fun prefix(prefix: String): CookingRecipeBuilder {
      this.prefix = prefix
      return this
    }

    fun suffix(suffix: String): CookingRecipeBuilder {
      this.suffix = suffix
      return this
    }

    fun cookingTime(time: Int): CookingRecipeBuilder {
      this.cookingTime = time
      return this
    }

    fun experience(exp: Float): CookingRecipeBuilder {
      this.exp = exp
      return this
    }

    fun unlockedBy(unlockedBy: Supplier<ItemPredicate>): CookingRecipeBuilder {
      this.unlockedBy = unlockedBy
      return this
    }

    fun inFurnace(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe("_smelting", SimpleCookingSerializer.SMELTING_RECIPE, cookingTime, exp) { b -> b }
      return this
    }

    fun inBlastFurnace(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe("_blasting", SimpleCookingSerializer.BLASTING_RECIPE, cookingTime, exp) { b -> b }
      return this
    }

    fun inSmoker(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe("_smoking", SimpleCookingSerializer.SMOKING_RECIPE, cookingTime, exp) { b -> b }
      return this
    }

    fun inCampfire(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      createCookingRecipe(
        "_campfire_cooking",
        SimpleCookingSerializer.CAMPFIRE_COOKING_RECIPE,
        cookingTime,
        exp
      ) { b -> b }
      return this
    }

    fun comboOreSmelting(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      this.inFurnace(cookingTime, exp)
      this.inBlastFurnace(cookingTime / 2, exp)
      return this
    }

    fun comboFoodCooking(
      cookingTime: Int = this.cookingTime,
      exp: Float = this.exp,
    ): CookingRecipeBuilder {
      this.inFurnace(cookingTime, exp)
      this.inSmoker(cookingTime / 2, exp)
      this.inCampfire(cookingTime / 2, exp)
      return this
    }

    protected fun <T : AbstractCookingRecipe> createCookingRecipe(
      suffixRecipe: String,
      serializer: RecipeSerializer<T>,
      cookingTime: Int,
      exp: Float,
      transform: UnaryOperator<SimpleCookingRecipeBuilder>,
    ): GeneratedRecipe {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = transform.apply(
          SimpleCookingRecipeBuilder.generic(
            ingredient?.get(),
            RecipeCategory.MISC,
            result?.get(),
            exp,
            (cookingTime * COOKING_TIME_MODIFIER).toInt(),
            serializer
          )
        )

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation(
            RegisteredObjects.getKeyOrThrow(serializer).path,
            result!!,
            prefix,
            suffix + suffixRecipe
          )
        )
      }

      allRecipes.add(generatedRecipe)

      return generatedRecipe
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }

  // STANDARD RECIPE CLASS
  class StandardRecipeBuilder(result: Supplier<ItemLike>, amount: Int) {
    private var result: Supplier<ItemLike>? = null
    private var amount: Int = 0
    private var unlockedBy: Supplier<ItemPredicate>? = null
    private var prefix: String = ""
    private var suffix: String = ""
    private val allRecipes: MutableList<GeneratedRecipe> = ArrayList()

    init {
      this.result = result
      this.amount = amount
    }

    fun apply(builder: UnaryOperator<StandardRecipeBuilder>): StandardRecipeBuilder {
      return builder.apply(this)
    }

    fun prefix(prefix: String): StandardRecipeBuilder {
      this.prefix = prefix
      return this
    }

    fun suffix(suffix: String): StandardRecipeBuilder {
      this.suffix = suffix
      return this
    }

    fun amount(amount: Int): StandardRecipeBuilder {
      this.amount = amount
      return this
    }

    fun unlockedBy(unlockedBy: Supplier<ItemPredicate>): StandardRecipeBuilder {
      this.unlockedBy = unlockedBy
      return this
    }

    // SHAPED
    fun shaped(
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    fun shaped(
      amount: Int,
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    fun shaped(
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapedBase(amount, prefix, suffix, builder)
    }

    protected fun shapedBase(
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapedRecipeBuilder>,
    ): StandardRecipeBuilder {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result?.get(), amount))

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation("crafting_shaped", result!!, prefix, suffix)
        )
      }

      allRecipes.add(generatedRecipe)

      return this
    }

    // SHAPELESS
    fun shapeless(
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    fun shapeless(
      amount: Int,
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    fun shapeless(
      amount: Int,
      prefix: String,
      suffix: String,
      requiresList: List<Ingredient>,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      return shapelessBase(requiresList, amount, prefix, suffix, builder)
    }

    protected fun shapelessBase(
      requiresList: List<Ingredient>,
      amount: Int,
      prefix: String,
      suffix: String,
      builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
    ): StandardRecipeBuilder {
      val generatedRecipe = GeneratedRecipe { consumer: Consumer<FinishedRecipe> ->
        val builder = builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result?.get(), amount))

        requiresList.forEach { ingredient ->
          builder.requires(ingredient)
        }

        if (unlockedBy != null) {
          builder.unlockedBy("has_item", inventoryTrigger(unlockedBy?.get()))
        }
        else {
          builder.unlockedBy("has_item",
            inventoryTrigger(ItemPredicate.Builder.item()
              .of(Items.AIR)
              .build()))
        }

        builder.save(
          { result -> consumer.accept(result) },
          createSimpleLocation("crafting_shapeless", result!!, prefix, suffix)
        )
      }

      allRecipes.add(generatedRecipe)

      return this
    }

    fun getRecipes(): List<GeneratedRecipe> {
      return allRecipes
    }
  }

  fun oreRecipes(
    name: String,
    ingot: ItemLike?,
    block: ItemLike?,
    nugget: ItemLike?,
    dust: ItemLike?,
    plate: ItemLike?,
    rawIngot: ItemLike?,
    crushedRaw: ItemLike?,
    rawBlock: ItemLike?,
    ores: List<OreBlockProcessor>?,
    washRemains: ItemLike?,
  ) {
    val hasOres = ores != null && ores.isNotEmpty()
    // smelt the raw ore into ingots
    if (ingot != null && rawIngot != null) {
      val SMELT_RAW = cooking({
        DataIngredient.tag(LibTags.forgeItemTag("raw_materials/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_raw")
          .comboOreSmelting(200, 0.5f)
      }
    }
    // smelt the raw block into a block
    if (block != null && rawBlock != null) {
      val SMELT_RAW_BLOCK = cooking({
        DataIngredient.tag(LibTags.forgeItemTag("storage_blocks/raw_$name"))
      },
        { block }) { b ->
        b
          .suffix("_from_raw_block")
          .comboOreSmelting(400, 4.5f)
      }
    }
    // smelt the ores into ingots
    if (ingot != null && hasOres) {
      val SMELT_ORE = cooking({
        DataIngredient.tag(LibTags.forgeItemTag("ores/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_ore")
          .comboOreSmelting(200, 1f)
      }
    }
    // smelt the crushed raw ore into ingots
    if (ingot != null && crushedRaw != null) {
      val SMELT_CRUSHED = cooking({
        DataIngredient.items(crushedRaw)
      },
        { ingot }) { b ->
        b
          .suffix("_from_crushed_raw")
          .comboOreSmelting(200, 1f)
      }
    }
    // wash the crushed raw ingot into nuggets
    if (nugget != null && crushedRaw != null) {
      if (washRemains != null) {
        val WASH_CRUSHED = create("$name" + "_from_crushed_raw") { b ->
          b
            .splashing { b ->
              b
                .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_$name")))
                .output(nugget, 9)
                .output(0.25f, washRemains, 1)
            }
        }
      }
      else {
        val WASH_CRUSHED = create("$name" + "_from_crushed_raw") { b ->
          b
            .splashing { b ->
              b
                .require(DataIngredient.tag(LibTags.createItemTag("crushed_raw_$name")))
                .output(nugget, 9)
            }
        }
      }
    }
    // smelt the dust into ingots
    if (ingot != null && dust != null) {
      val SMELT_DUST = cooking({
        DataIngredient.tag(LibTags.forgeItemTag("dusts/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_dust")
          .comboOreSmelting(200, 0.5f)
      }
    }
    // wash the dust into nuggets
    if (nugget != null && dust != null) {
      val WASH_DUST = create("$name" + "_from_dust") { b ->
        b
          .splashing { b ->
            b
              .require(DataIngredient.tag(LibTags.forgeItemTag("dusts/$name")))
              .output(nugget, 9)
          }
      }
    }
    // create dust from crushing ingot
    if (dust != null && ingot != null) {
      val CRUSH_INGOT = create("$name" + "_dust_from_ingot") { b ->
        b
          .milling { b ->
            b
              .require(LibTags.forgeItemTag("ingots/$name"))
              .duration(120)
              .output(dust, 1)
          }
          .crushing { b ->
            b
              .require(LibTags.forgeItemTag("ingots/$name"))
              .duration(120)
              .output(dust, 1)
          }
      }
    }
    // smelt the plate into nuggets
    if (nugget != null && plate != null) {
      val SMELT_PLATE = cooking({
        DataIngredient.tag(LibTags.forgeItemTag("plates/$name"))
      },
        { nugget }) { b ->
        b
          .suffix("_from_plate")
          .comboOreSmelting(200, 0.5f)
      }
    }
    // create plate from pressing ingot
    if (plate != null && ingot != null) {
      val PRESS_INGOT = create("$name" + "_from_ingot") { b ->
        b
          .pressing { b ->
            b
              .require(LibTags.forgeItemTag("ingots/$name"))
              .output(plate, 1)
          }
      }
    }
    // crush the raw block into crushed raw
    if (crushedRaw != null && rawBlock != null) {
      val CRUSH_RAW_BLOCK = create("crushed_raw_$name" + "_from_raw_block") { b ->
        b
          .crushing { b ->
            b
              .require(rawBlock)
              .duration(120)
              .output(crushedRaw, 9)
              .output(0.75f, AllItems.EXP_NUGGET.get(), 9)
          }
      }
    }
    // crush the raw ore into crushed raw
    if (crushedRaw != null && rawIngot != null) {
      val CRUSH_RAW = create("crushed_raw_$name" + "_from_raw") { b ->
        b
          .crushing { b ->
            b
              .require(rawIngot)
              .duration(120)
              .output(crushedRaw, 1)
              .output(0.75f, AllItems.EXP_NUGGET.get(), 1)
          }
      }
    }
    // crush the stone ore into crushed raw
    if (crushedRaw != null && hasOres && ores != null) {
      for (ore in ores) {
        val CRUSH_ORE =
          create("crushed_raw_$name" + "_from_${RegisteredObjects.getKeyOrThrow(ore.oreBlock.asItem()).path}") { b ->
            b
              .crushing { b ->
                b
                  .require(ore.oreBlock)
                  .duration(120)
                  .output(crushedRaw, ore.multiplier)
                  .output(0.75f, crushedRaw, 1)
                  .output(0.75f, AllItems.EXP_NUGGET.get(), 1)
                  .output(0.125f, ore.stoneBlock, 1)
              }
          }
      }
    }
  }

  class OreBlockProcessor(oreBlock: ItemLike, stoneBlock: ItemLike, multiplier: Int) {
    val oreBlock = oreBlock
    val stoneBlock = stoneBlock
    val multiplier = multiplier
  }
}