package com.jerry.mekmm.common.config;

import com.jerry.mekmm.common.util.ValidatorUtils;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoreMachineGeneralConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;

    public final CachedConfigValue<List<? extends String>> itemReplicatorRecipe;
    public final CachedConfigValue<List<? extends String>> fluidReplicatorRecipe;

    MoreMachineGeneralConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");

        builder.comment("Custom Replicator Recipes").push("replicator_recipes");
        itemReplicatorRecipe = CachedConfigValue.wrap(this, builder.comment("The recipes added here will be added to the item replicator. Write using modid:registeredName#amount, # followed by the amount(not null or zero) of UU matter consumed. For example:[\"minecraft:stone#10\",\"mekanism:basic_bin#100\"]")
                .defineListAllowEmpty(Collections.singletonList("itemReplicatorRecipe"), ArrayList::new, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        fluidReplicatorRecipe = CachedConfigValue.wrap(this, builder.comment("The recipes added here will be added to the fluid replicator. Write using modid:registeredName#amount, # followed by the amount(not null or zero) of UU matter consumed. For example:[\"minecraft:water#10\",\"mekanism:heavy_water#100\"]")
                .defineListAllowEmpty("fluidReplicatorRecipe", ArrayList::new, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
