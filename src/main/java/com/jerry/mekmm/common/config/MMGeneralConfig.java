package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.util.ValidatorUtils;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class MMGeneralConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

//    public final CachedLongValue conversionMultiplier;

    public final CachedConfigValue<List<? extends String>> itemDuplicatorRecipe;
    public final CachedConfigValue<List<? extends String>> fluidDuplicatorRecipe;
    public final CachedConfigValue<List<? extends String>> chemicalDuplicatorRecipe;

    MMGeneralConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        MMConfigTranslations.GENERAL_REPLICATOR_RECIPES.applyToBuilder(builder).push("replicator_recipes");
        itemDuplicatorRecipe = CachedConfigValue.wrap(this, MMConfigTranslations.ITEM_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("itemDuplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        fluidDuplicatorRecipe = CachedConfigValue.wrap(this, MMConfigTranslations.FLUID_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("fluidDuplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        chemicalDuplicatorRecipe = CachedConfigValue.wrap(this, MMConfigTranslations.CHEMICAL_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("chemicalDuplicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        builder.pop();

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
