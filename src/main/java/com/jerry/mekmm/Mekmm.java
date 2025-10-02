package com.jerry.mekmm;

import com.jerry.mekaf.common.registries.AFBlocks;
import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekaf.common.registries.AFTileEntityTypes;
import com.jerry.mekmm.common.network.MoreMachinePacketHandler;
import com.jerry.mekmm.common.registries.*;
import com.mojang.logging.LogUtils;
import mekanism.common.base.IModModule;
import mekanism.common.lib.Version;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Mekmm.MOD_ID)
public class Mekmm implements IModModule {

    public static final String MOD_ID = "mekmm";
    public static final String MOD_NAME = "MekanismMoreMachine";
    public static final Logger LOGGER = LogUtils.getLogger();
    /**
     * Mekanism: MoreMachine Packet Pipeline
     */
    private final MoreMachinePacketHandler packetHandler;
    /**
     * Mekanism: MoreMachine mod instance
     */
    public static Mekmm instance;
    /**
     * Mekanism: MoreMachine version number
     */
    public final Version versionNumber;

    public Mekmm(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ModContainer modContainer = context.getContainer();
        instance = this;
        versionNumber = new Version(modContainer);

        MMItems.MM_ITEMS.register(modEventBus);
        MMBlocks.MM_BLOCKS.register(modEventBus);
        MMTileEntityTypes.MM_TILE_ENTITY_TYPES.register(modEventBus);
        MMContainerTypes.MM_CONTAINER_TYPES.register(modEventBus);
        MMCreativeTabs.MM_CREATIVE_TABS.register(modEventBus);
        MMRecipeSerializers.MM_RECIPE_SERIALIZERS.register(modEventBus);

        registerAdvancedFactory(modEventBus);

        packetHandler = new MoreMachinePacketHandler();
    }

    public static MoreMachinePacketHandler packetHandler() {
        return instance.packetHandler;
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void registerAdvancedFactory(IEventBus modEventBus) {
        AFBlocks.AF_BLOCKS.register(modEventBus);
        AFTileEntityTypes.AF_TILE_ENTITY_TYPES.register(modEventBus);
        AFContainerTypes.AF_CONTAINER_TYPES.register(modEventBus);
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "MoreMachine";
    }

    @Override
    public void resetClient() {

    }
}
