package net.Rampage.mob_block_farming.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record BlenderRecipe(Ingredient inputItem, ItemStack output) implements Recipe<BlenderRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    // read in JSON File --> turns into new BlenderRecipe

    @Override
    public boolean matches(BlenderRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItem.test(pInput.getItem(0));
    }

    @Override
    public ItemStack assemble(BlenderRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BLENDER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BLENDER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<BlenderRecipe> {
        public static final MapCodec<BlenderRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BlenderRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(BlenderRecipe::output)
        ).apply(inst, BlenderRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BlenderRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, BlenderRecipe::inputItem,
                        ItemStack.STREAM_CODEC, BlenderRecipe::output,
                        BlenderRecipe::new);

        @Override
        public MapCodec<BlenderRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlenderRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
