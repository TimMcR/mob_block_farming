package net.Rampage.mob_block_farming.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record HarvesterRecipeInput(String mobType, String harvesterType, int foodCost) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public int size() {
        return 3;
    }
}
