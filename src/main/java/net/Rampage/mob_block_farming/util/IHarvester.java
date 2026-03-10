package net.Rampage.mob_block_farming.util;

import net.minecraft.world.item.ItemStack;

public interface IHarvester {
    int getFoodPointCost();

    void acceptHarvesterOutput(ItemStack stack);
}
