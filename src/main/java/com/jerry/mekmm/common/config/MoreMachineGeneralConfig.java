package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.util.ValidatorUtils;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;

public class MoreMachineGeneralConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    //Replicator
    public final CachedConfigValue<List<? extends String>> itemReplicatorRecipe;
    public final CachedConfigValue<List<? extends String>> fluidReplicatorRecipe;
    public final CachedConfigValue<List<? extends String>> chemicalReplicatorRecipe;

    public final CachedIntValue gasCollectAmount;
    public final CachedLongValue wirelessChargingStationChargingRate;

    MoreMachineGeneralConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        MoreMachineConfigTranslations.GENERAL_REPLICATOR_RECIPES.applyToBuilder(builder).push("replicator_recipes");
        itemReplicatorRecipe = CachedConfigValue.wrap(this, MoreMachineConfigTranslations.ITEM_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("itemReplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        fluidReplicatorRecipe = CachedConfigValue.wrap(this, MoreMachineConfigTranslations.FLUID_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("fluidReplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        chemicalReplicatorRecipe = CachedConfigValue.wrap(this, MoreMachineConfigTranslations.CHEMICAL_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("chemicalReplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        builder.pop();

        gasCollectAmount = CachedIntValue.wrap(this, MoreMachineConfigTranslations.GAS_COLLECT_AMOUNT.applyToBuilder(builder).defineInRange("gasCollectAmount", 1, 1, FluidType.BUCKET_VOLUME));
        wirelessChargingStationChargingRate = CachedLongValue.definePositive(this, builder, MoreMachineConfigTranslations.WIRELESS_CHARGING_STATION_CHARGING_RATE, "wirelessChargingStationChargingRate", 100_000L);

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public String getTranslation() {
        return "General Config";
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
