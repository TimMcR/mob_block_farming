package net.Rampage.mob_block_farming.screen;

import net.Rampage.mob_block_farming.MobBlockFarming;
import net.Rampage.mob_block_farming.screen.custom.BlenderMenu;
import net.Rampage.mob_block_farming.screen.custom.MeatHarvesterMenu;
import net.Rampage.mob_block_farming.screen.custom.TroughMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MobBlockFarming.MOD_ID);
    public static final RegistryObject<MenuType<BlenderMenu>> BLENDER_MENU =
            MENUS.register("blender_name", () -> IForgeMenuType.create(BlenderMenu::new));

    public static final RegistryObject<MenuType<TroughMenu>> TROUGH_MENU =
            MENUS.register("trough_name", () -> IForgeMenuType.create(TroughMenu::new));

    public static final RegistryObject<MenuType<MeatHarvesterMenu>> MEAT_HARVESTER_MENU =
            MENUS.register("meat_harvester_name", () -> IForgeMenuType.create(MeatHarvesterMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}