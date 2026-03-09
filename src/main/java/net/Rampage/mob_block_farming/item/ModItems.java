package net.Rampage.mob_block_farming.item;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.item.custom.SlurryItem;
import net.Rampage.mob_block_farming.util.SlurryType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MobBlockFarming.MOD_ID);

    public static final RegistryObject<Item> ROTARY_BLADE =
            ITEMS.register("rotary_blade", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VEGAN_SLURRY =
            ITEMS.register("vegan_slurry", () -> new SlurryItem(new Item.Properties(), SlurryType.VEGAN));
    public static final RegistryObject<Item> MEAT_SLURRY =
            ITEMS.register("meat_slurry", () -> new SlurryItem(new Item.Properties(), SlurryType.MEAT));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
