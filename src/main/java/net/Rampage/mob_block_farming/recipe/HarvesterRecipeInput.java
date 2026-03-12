package net.Rampage.mob_block_farming.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record HarvesterRecipeInput(String mobType, String harvesterType) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return mobType.isEmpty() || harvesterType.isEmpty();
    }
}
