package com.jerry.mekmm.common.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class MMStorageConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue recycler;
    public final CachedLongValue plantingStation;
    public final CachedLongValue cnc_stamper;
    public final CachedLongValue cnc_lathe;
    public final CachedLongValue cnc_rollingMill;
    public final CachedLongValue replicator;
    public final CachedLongValue ambientGasCollector;

    MMStorageConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        recycler = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_RECYCLER, "recycler", 20_000L, 1);
        plantingStation = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_PLANTING_STATION, "plantingStation", 80_000L, 1);
        cnc_stamper = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_CNC_STAMPER, "cnc_stamper", 20_000L, 1);
        cnc_lathe = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_CNC_LATHE, "cnc_lathe", 20_000L, 1);
        cnc_rollingMill = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_ROLLING_MILL, "cnc_rollingMill", 20_000L, 1);
        replicator = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_REPLICATOR, "replicator", 102_400_000L, 1);
        ambientGasCollector = CachedLongValue.definedMin(this, builder, MMConfigTranslations.ENERGY_STORAGE_AMBIENT_GAS_COLLECTOR, "ambientGasCollector", 40_000L, 1);

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "machine-storage";
    }

    @Override
    public String getTranslation() {
        return "Storage Config";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
