package net.Rampage.mob_block_farming.recipe;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MobBlockFarming.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MobBlockFarming.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BlenderRecipe>> BLENDER_SERIALIZER =
            SERIALIZERS.register("blender", BlenderRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<BlenderRecipe>> BLENDER_TYPE =
            TYPES.register("blender", () -> new RecipeType<BlenderRecipe>() {
                @Override
                public String toString() {
                    return "blender";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
