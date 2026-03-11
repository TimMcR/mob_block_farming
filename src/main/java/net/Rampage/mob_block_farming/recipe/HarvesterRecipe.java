package net.Rampage.mob_block_farming.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Objects;

public record HarvesterRecipe(String mobType, String harvesterType, int foodCost, ItemStack result) implements Recipe<HarvesterRecipeInput> {
    @Override
    public boolean matches(HarvesterRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return  Objects.equals(pInput.mobType(), mobType) &&
                Objects.equals(pInput.harvesterType(), harvesterType);
    }

    @Override
    public ItemStack assemble(HarvesterRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.HARVESTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.HARVESTER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<HarvesterRecipe> {

        @Override
        public MapCodec<HarvesterRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HarvesterRecipe> streamCodec() {
            return null;
        }
    }
}
