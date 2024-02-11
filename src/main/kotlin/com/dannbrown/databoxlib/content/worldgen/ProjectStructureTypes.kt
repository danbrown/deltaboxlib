package com.dannbrown.databoxlib.content.worldgen

import com.dannbrown.databoxlib.ProjectContent
import com.dannbrown.databoxlib.content.worldgen.structures.ArchPiece
import com.dannbrown.databoxlib.content.worldgen.structures.ArchStructure
import com.dannbrown.databoxlib.content.worldgen.structures.PlatformStructure
import com.dannbrown.databoxlib.content.worldgen.structures.PlatformStructurePiece
import com.mojang.serialization.Codec
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier


object ProjectStructureTypes {
  val STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, ProjectContent.MOD_ID)
  val STRUCTURE_PIECE = DeferredRegister.create(Registries.STRUCTURE_PIECE, ProjectContent.MOD_ID)

  fun register(modBus: IEventBus){
    STRUCTURE_TYPE.register(modBus)
    STRUCTURE_PIECE.register(modBus)
  }

  // @ STRUCTURE TYPES
  val PLATFORM  = registerStructureType("boulder") { PlatformStructure.CODEC }
  val ARCH  = registerStructureType("arch") { ArchStructure.CODEC }

  // @ STRUCTURE PIECES
  val PLATFORM_PIECE = registerStructurePiece("boulder_piece", ::PlatformStructurePiece)
  val ARCH_PIECE = registerStructurePiece("arch_piece", ::ArchPiece)


  // registration
  private fun <S : Structure?> registerStructureType(id: String, codec: Supplier<out Codec<S>?>): RegistryObject<StructureType<S>> {
    return STRUCTURE_TYPE.register(
      id
    ) { StructureType { codec.get() } }
  }
   private fun  registerStructurePiece(id: String, structurePiece: StructurePieceType): RegistryObject<StructurePieceType> {
     return STRUCTURE_PIECE.register(
       id
     ) { structurePiece }
   }

}