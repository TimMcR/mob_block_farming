package net.Rampage.mob_block_farming.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
        return Objects.equals(pInput.mobType(), mobType) &&
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
        public static final MapCodec<HarvesterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.fieldOf("mob_type").forGetter(HarvesterRecipe::mobType),
                Codec.STRING.fieldOf("harvester_type").forGetter(HarvesterRecipe::harvesterType),
                Codec.INT.fieldOf("food_cost").forGetter(HarvesterRecipe::foodCost),
                ItemStack.CODEC.fieldOf("result").forGetter(HarvesterRecipe::result)
            ).apply(inst, HarvesterRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, HarvesterRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, HarvesterRecipe::mobType,
                        ByteBufCodecs.STRING_UTF8, HarvesterRecipe::harvesterType,
                        ByteBufCodecs.INT, HarvesterRecipe::foodCost,
                        ItemStack.STREAM_CODEC, HarvesterRecipe::result,
                        HarvesterRecipe::new
                );

        @Override
        public MapCodec<HarvesterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HarvesterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
