package net.Rampage.mob_block_farming;

import net.Rampage.mob_block_farming.util.SlurryType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class BlenderRecipeData {
    public record BlenderResult(SlurryType type, int amount) {}

    private static final Map<Item, BlenderResult> RECIPES = new HashMap<>();

    static {
        RECIPES.put(Items.PUMPKIN, new BlenderResult(SlurryType.VEGAN, 10));
        RECIPES.put(Items.MELON, new BlenderResult(SlurryType.VEGAN, 10));
        RECIPES.put(Items.APPLE, new BlenderResult(SlurryType.VEGAN, 4));
        RECIPES.put(Items.CARROT, new BlenderResult(SlurryType.VEGAN, 3));
        RECIPES.put(Items.POTATO, new BlenderResult(SlurryType.VEGAN, 3));
        RECIPES.put(Items.WHEAT, new BlenderResult(SlurryType.VEGAN, 2));
        RECIPES.put(Items.BEETROOT, new BlenderResult(SlurryType.VEGAN, 2));
        RECIPES.put(Items.WHEAT_SEEDS, new BlenderResult(SlurryType.VEGAN, 1));
        RECIPES.put(Items.BEETROOT_SEEDS, new BlenderResult(SlurryType.VEGAN, 1));
        RECIPES.put(Items.MELON_SEEDS, new BlenderResult(SlurryType.VEGAN, 1));
        RECIPES.put(Items.PUMPKIN_SEEDS, new BlenderResult(SlurryType.VEGAN, 1));

        RECIPES.put(Items.BEEF, new BlenderResult(SlurryType.MEAT, 5));
        RECIPES.put(Items.PORKCHOP, new BlenderResult(SlurryType.MEAT, 5));
        RECIPES.put(Items.MUTTON, new BlenderResult(SlurryType.MEAT, 5));
        RECIPES.put(Items.ROTTEN_FLESH, new BlenderResult(SlurryType.MEAT, 2));
        RECIPES.put(Items.COOKED_BEEF, new BlenderResult(SlurryType.MEAT, 1));
        RECIPES.put(Items.COOKED_PORKCHOP, new BlenderResult(SlurryType.MEAT, 1));
        RECIPES.put(Items.COOKED_MUTTON, new BlenderResult(SlurryType.MEAT, 1));
    }

    public static BlenderResult get(Item item) {
        return RECIPES.get(item);
    }
}
