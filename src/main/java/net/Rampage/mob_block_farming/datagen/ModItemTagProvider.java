package net.Rampage.mob_block_farming.datagen;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, tagLookupCompletableFuture, MobBlockFarming.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        addMultipleTags(ModItems.VEGAN_SLURRY.get(),
                List.of(ItemTags.COW_FOOD, ItemTags.CHICKEN_FOOD, ItemTags.ARMADILLO_FOOD, ItemTags.CAMEL_FOOD,
                        ItemTags.GOAT_FOOD, ItemTags.HORSE_FOOD, ItemTags.LLAMA_FOOD, ItemTags.PANDA_FOOD,
                        ItemTags.PARROT_FOOD, ItemTags.PIG_FOOD, ItemTags.RABBIT_FOOD, ItemTags.SHEEP_FOOD,
                        ItemTags.SNIFFER_FOOD, ItemTags.TURTLE_FOOD));

        addMultipleTags(ModItems.MEAT_SLURRY.get(),
                List.of(ItemTags.FOX_FOOD, ItemTags.HOGLIN_FOOD, ItemTags.PIGLIN_FOOD, ItemTags.WOLF_FOOD));
    }

    private void addMultipleTags(Item item, List<TagKey<Item>> tags) {
        tags.forEach(tag -> tag(tag).add(item));
    }
}
