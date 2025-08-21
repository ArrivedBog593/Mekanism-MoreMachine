package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import mekanism.common.config.IMekanismConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MoreMachineConfig {

    private MoreMachineConfig() {
    }

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    public static final MoreMachineGeneralConfig general = new MoreMachineGeneralConfig();
    public static final MoreMachineStorageConfig storage = new MoreMachineStorageConfig();
    public static final MoreMachineUsageConfig usage = new MoreMachineUsageConfig();

    public static void registerConfigs(ModContainer modContainer) {
        MoreMachineHelper.registerConfig(KNOWN_CONFIGS, modContainer, general);
        MoreMachineHelper.registerConfig(KNOWN_CONFIGS, modContainer, storage);
        MoreMachineHelper.registerConfig(KNOWN_CONFIGS, modContainer, usage);
    }

    public static void onConfigLoad(ModConfigEvent configEvent) {
        MoreMachineHelper.onConfigLoad(configEvent, Mekmm.MOD_ID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}
