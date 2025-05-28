package com.jerry.mekmm;

import com.jerry.mekaf.common.registries.AFBlocks;
import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekaf.common.registries.AFTileEntityTypes;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.network.MMPacketHandler;
import com.jerry.mekmm.common.registries.*;
import com.mojang.logging.LogUtils;
import mekanism.common.lib.Version;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Mekmm.MOD_ID)
public class Mekmm {
    public static final String MOD_ID = "mekmm";
    public static final String MOD_NAME = "MekanismMoreMachine";
    public static final Logger LOGGER = LogUtils.getLogger();

    private final MMPacketHandler packetHandler;

    public static Mekmm instance;

    public final Version versionNumber;

    public Mekmm(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        //Set our version number to match the neoforge.mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(modContainer);

        // MoreMachine相关的注册
        MMConfig.registerConfigs(modContainer);
        MMItems.MM_ITEMS.register(modEventBus);
        MMBlocks.MM_BLOCKS.register(modEventBus);
        MMTileEntityTypes.MM_TILE_ENTITY_TYPES.register(modEventBus);
        MMContainerTypes.MM_CONTAINER_TYPES.register(modEventBus);
        MMRecipeSerializersInternal.MM_RECIPE_SERIALIZERS.register(modEventBus);
        MMChemicals.MM_CHEMICALS.register(modEventBus);
        MMCreativeTabs.MM_CREATIVE_TABS.register(modEventBus);
        modEventBus.addListener(MMConfig::onConfigLoad);
        // LargeMachine相关的注册
//        LMConfig.registerConfigs(modContainer);
        registerAdvancedFactory(modEventBus);

        packetHandler = new MMPacketHandler(modEventBus, versionNumber);
    }

    public static MMPacketHandler packetHandler() {
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
}
