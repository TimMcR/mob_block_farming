package net.Rampage.mob_block_farming.datagen;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MobBlockFarming.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.ROTARY_BLADE.get());
        basicItem(ModItems.VEGAN_SLURRY.get());
        basicItem(ModItems.MEAT_SLURRY.get());
    }
}
