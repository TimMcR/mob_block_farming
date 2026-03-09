package net.Rampage.mob_block_farming;

import com.mojang.logging.LogUtils;
import net.Rampage.mob_block_farming.block.ModBlocks;
import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.item.ModItems;
import net.Rampage.mob_block_farming.recipe.ModRecipes;
import net.Rampage.mob_block_farming.screen.ModMenuTypes;
import net.Rampage.mob_block_farming.screen.custom.BlenderScreen;
import net.Rampage.mob_block_farming.screen.custom.TroughScreen;
import net.Rampage.mob_block_farming.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MobBlockFarming.MOD_ID)
public class MobBlockFarming
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mob_block_farming";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MobBlockFarming()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(ModItems.VEGAN_SLURRY.get(), 0.5f);
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS)
            AddIngredientsToTab(event);

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            AddBuildingBlocksToTab(event);

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
        {
            event.accept(ModItems.ROTARY_BLADE);
            event.accept(ModBlocks.BLENDER);
        }
    }

    private void AddIngredientsToTab(BuildCreativeModeTabContentsEvent event) {
        event.accept(ModItems.VEGAN_SLURRY);
        event.accept(ModItems.MEAT_SLURRY);
    }

    private void AddBuildingBlocksToTab(BuildCreativeModeTabContentsEvent event) {
        event.accept(ModBlocks.TROUGH);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.BLENDER_MENU.get(), BlenderScreen::new);
            MenuScreens.register(ModMenuTypes.TROUGH_MENU.get(), TroughScreen::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {

        }
    }
}
