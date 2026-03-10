package net.Rampage.mob_block_farming.block;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.block.custom.BlenderBlock;
import net.Rampage.mob_block_farming.block.custom.MeatHarvesterBlock;
import net.Rampage.mob_block_farming.block.custom.TroughBlock;
import net.Rampage.mob_block_farming.block.custom.PigBlock;
import net.Rampage.mob_block_farming.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MobBlockFarming.MOD_ID);

    public static final RegistryObject<Block> BLENDER =
            registerBlock("blender", () -> new BlenderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> TROUGH =
            registerBlock("trough", () -> new TroughBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> PIG_BLOCK = registerBlock("pig_block", () ->
            new PigBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_WOOL)));

    public static final RegistryObject<Block> MEAT_HARVESTER_BLOCK = registerBlock("meat_harvester_block", () ->
            new MeatHarvesterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> result = BLOCKS.register(name, block);
        registerBlockItem(name, result);

        return result;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
