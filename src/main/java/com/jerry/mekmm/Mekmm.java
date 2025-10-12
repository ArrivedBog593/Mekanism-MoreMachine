package com.jerry.mekmm;

import com.jerry.mekaf.common.registries.AdvancedFactoryBlocks;
import com.jerry.mekaf.common.registries.AdvancedFactoryContainerTypes;
import com.jerry.mekaf.common.registries.AdvancedFactoryTileEntityTypes;
import com.jerry.meklm.common.registries.LargeMachineBlocks;
import com.jerry.meklm.common.registries.LargeMachineContainerTypes;
import com.jerry.meklm.common.registries.LargeMachineTileEntityTypes;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.network.MoreMachinePacketHandler;
import com.jerry.mekmm.common.registries.*;
import com.mojang.logging.LogUtils;
import mekanism.common.Mekanism;
import mekanism.common.base.IModModule;
import mekanism.common.lib.Version;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Mekmm.MOD_ID)
public class Mekmm implements IModModule {
    public static final String MOD_ID = "mekmm";
    public static final String MOD_NAME = "MekanismMoreMachine";
    public static final Logger LOGGER = LogUtils.getLogger();

    private final MoreMachinePacketHandler packetHandler;

    public static Mekmm instance;

    public final Version versionNumber;

    public Mekmm(IEventBus modEventBus, ModContainer modContainer) {
        Mekanism.addModule(instance = this);
        //Set our version number to match the neoforge.mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(modContainer);

        // MoreMachine相关的注册
        MoreMachineConfig.registerConfigs(modContainer);
        MoreMachineItems.MM_ITEMS.register(modEventBus);
        MoreMachineBlocks.MM_BLOCKS.register(modEventBus);
        MoreMachineTileEntityTypes.MM_TILE_ENTITY_TYPES.register(modEventBus);
        MoreMachineContainerTypes.MM_CONTAINER_TYPES.register(modEventBus);
        MoreMachineRecipeSerializersInternal.MM_RECIPE_SERIALIZERS.register(modEventBus);
        MoreMachineChemicals.MM_CHEMICALS.register(modEventBus);
        MoreMachineCreativeTabs.MM_CREATIVE_TABS.register(modEventBus);
        modEventBus.addListener(MoreMachineConfig::onConfigLoad);
        // LargeMachine相关的注册
//        LMConfig.registerConfigs(modContainer);
        registerAdvancedFactory(modEventBus);
        registerLargeMachine(modEventBus);

        packetHandler = new MoreMachinePacketHandler(modEventBus, versionNumber);
    }

    public static MoreMachinePacketHandler packetHandler() {
        return instance.packetHandler;
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void registerAdvancedFactory(IEventBus modEventBus) {
        AdvancedFactoryBlocks.AF_BLOCKS.register(modEventBus);
        AdvancedFactoryTileEntityTypes.AF_TILE_ENTITY_TYPES.register(modEventBus);
        AdvancedFactoryContainerTypes.AF_CONTAINER_TYPES.register(modEventBus);
    }

    private void registerLargeMachine(IEventBus modEventBus) {
        LargeMachineBlocks.LM_BLOCKS.register(modEventBus);
        LargeMachineTileEntityTypes.LM_TILE_ENTITY_TYPES.register(modEventBus);
        LargeMachineContainerTypes.LM_CONTAINER_TYPES.register(modEventBus);
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "MoreMachine";
    }
}
