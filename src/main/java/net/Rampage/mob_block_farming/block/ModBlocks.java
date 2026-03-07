package net.Rampage.mob_block_farming.block;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.block.custom.MobBlock;
import net.Rampage.mob_block_farming.item.ModItems;
import net.Rampage.mob_block_farming.util.MobType;
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

//    public static final RegistryObject<Block> ALEXANDRITE_BLOCK =
//            registerBlock("alexandrite_block", () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
//
//    public static final RegistryObject<Block> RAW_ALEXANDRITE_BLOCK =
//            registerBlock("raw_alexandrite_block", () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
//
//    public static final RegistryObject<Block> ALEXANDRITE_ORE =
//            registerBlock("alexandrite_ore", () -> new DropExperienceBlock(UniformInt.of(2, 4),
//                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
//
//    public static final RegistryObject<Block> ALEXANDRITE_DEEPSLATE_ORE =
//            registerBlock("alexandrite_deepslate_ore", () -> new DropExperienceBlock(UniformInt.of(3, 6),
//                    BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
//
//    public static final RegistryObject<Block> MAGIC_BLOCK =
//            registerBlock("magic_block", () -> new MagicBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));
//
//    public static final RegistryObject<Block> PEDESTAL = registerBlock("pedestal", () -> new PedestalBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> PIG_BLOCK = registerBlock("pig_block", () -> new MobBlock(MobType.PIG, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_WOOL)));

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
