package net.Rampage.mob_block_farming.datagen;

import net.Rampage.mob_block_farming.block.ModBlocks;
import net.Rampage.mob_block_farming.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
//        dropSelf(ModBlocks.ALEXANDRITE_BLOCK.get());
//        dropSelf(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());
//        dropSelf(ModBlocks.MAGIC_BLOCK.get());
//        dropSelf(ModBlocks.PEDESTAL.get());
//
//        this.add(ModBlocks.ALEXANDRITE_ORE.get(),
//                block -> createOreDrop(ModBlocks.ALEXANDRITE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));
//        this.add(ModBlocks.ALEXANDRITE_DEEPSLATE_ORE.get(),
//                block -> createOreDrop(ModBlocks.ALEXANDRITE_DEEPSLATE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));

        dropSelf(ModBlocks.BLENDER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
