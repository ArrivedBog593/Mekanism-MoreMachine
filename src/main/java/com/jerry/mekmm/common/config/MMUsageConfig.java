package com.jerry.mekmm.common.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class MMUsageConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue recycler;
    public final CachedLongValue plantingStation;
    public final CachedLongValue cnc_stamper;
    public final CachedLongValue cnc_lathe;
    public final CachedLongValue cnc_rollingMill;
    public final CachedLongValue replicator;
    public final CachedLongValue ambientGasCollector;

    MMUsageConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        recycler = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_RECYCLER, "recycler", 50L);
        plantingStation = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_PLANTING_STATION, "plantingStation", 50L);
        cnc_stamper = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_CNC_STAMPER, "cnc_stamper", 50L);
        cnc_lathe = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_CNC_LATHE, "cnc_lathe", 50L);
        cnc_rollingMill = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_ROLLING_MILL, "cnc_rollingMill", 50L);
        replicator = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_REPLICATOR, "replicator", 102_400L);
        ambientGasCollector = CachedLongValue.definePositive(this, builder, MMConfigTranslations.ENERGY_USAGE_AMBIENT_GAS_COLLECTOR, "ambientGasCollector", 100L);

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "machine-usage";
    }

    @Override
    public String getTranslation() {
        return "Usage Config";
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
