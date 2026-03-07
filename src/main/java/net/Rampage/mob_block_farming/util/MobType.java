package net.Rampage.mob_block_farming.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public enum MobType {
    PIG(Set.of(Items.CARROT, Items.POTATO));

    private final Set<Item> diet;

    MobType(Set<Item> diet) {
        this.diet = diet;
    }

    public boolean canEat(ItemStack itemStack) {
        return diet.contains(itemStack.getItem());
    }
}
