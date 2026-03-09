package net.Rampage.mob_block_farming.block.entity;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.block.ModBlocks;
import net.Rampage.mob_block_farming.block.entity.custom.BlenderBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MobBlockFarming.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlenderBlockEntity>> BLENDER_BE =
            BLOCK_ENTITIES.register("blender_be", () -> BlockEntityType.Builder.of(
                    BlenderBlockEntity::new, ModBlocks.BLENDER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
