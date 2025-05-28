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

public class MMConfig {

    private MMConfig() {
    }

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    public static final MMGeneralConfig general = new MMGeneralConfig();
    public static final MMStorageConfig storage = new MMStorageConfig();
    public static final MMUsageConfig usage = new MMUsageConfig();

    public static void registerConfigs(ModContainer modContainer) {
        MMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, general);
        MMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, storage);
        MMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, usage);
    }

    public static void onConfigLoad(ModConfigEvent configEvent) {
        MMConfigHelper.onConfigLoad(configEvent, Mekmm.MOD_ID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}
