package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import mekanism.common.config.IConfigTranslation;
import mekanism.common.config.TranslationPreset;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MoreMachineConfigTranslations implements IConfigTranslation {

    GENERAL_ENERGY_CONVERSION("general.energy_conversion", "Energy Conversion Rate", "Settings for configuring Energy Conversions", "Edit Conversion Rates"),
    GENERAL_ENERGY_CONVERSION_MULTIPLIER("general.energy_conversion.conversion_multiplier", "Conversion Multiplier",
            "How much energy is produced per mB of Hydrogen, also affects Electrolytic Separator usage, Ethene burn rate and Gas-Burning Generator energy capacity."),

    GENERAL_REPLICATOR_RECIPES("general.replicator", "Replicator Recipes", "Custom Replicator Recipes"),
    ITEM_RECIPES("general.item_replicator.recipes", "Add Item Replicator Recipes", "The recipes added here will be added to the item replicator. Write using modid:registeredName#amount, # followed by the amount(not null or zero) of UU matter consumed. For example:[\"minecraft:stone#10\",\"mekanism:basic_bin#100\"]"),
    FLUID_RECIPES("general.fluid_replicator.recipes", "Add Fluid Replicator Recipes", "The recipes added here will be added to the fluid replicator. Write using modid:registeredName#amount, # followed by the amount(not null or zero) of UU matter consumed. For example:[\"minecraft:water#10\",\"mekanism:heavy_water#100\"]"),
    CHEMICAL_RECIPES("general.chemical_replicator.recipes", "Add Chemical Replicator Recipes", "The recipes added here will be added to the chemical replicator. Write using modid:registeredName#amount, # followed by the amount(not null or zero) of UU matter consumed. For example:[\"mekanism:oxygen#10\",\"mekanism:hydrogen#100\"]"),

    //Storage Config
    ENERGY_STORAGE_RECYCLER(TranslationPreset.ENERGY_STORAGE, "Recycler"),
    ENERGY_STORAGE_PLANTING_STATION(TranslationPreset.ENERGY_STORAGE, "Planting Station"),
    ENERGY_STORAGE_CNC_STAMPER(TranslationPreset.ENERGY_STORAGE, "CNC Stamper"),
    ENERGY_STORAGE_CNC_LATHE(TranslationPreset.ENERGY_STORAGE, "CNC Lathe"),
    ENERGY_STORAGE_ROLLING_MILL(TranslationPreset.ENERGY_STORAGE, "CNC Rolling Mill"),
    ENERGY_STORAGE_AMBIENT_GAS_COLLECTOR(TranslationPreset.ENERGY_STORAGE, "Ambient Gas Collector"),
    ENERGY_STORAGE_REPLICATOR("storage.replicator", "Replicator", "Settings for configuring Replicator Energy Storage", true),
    ENERGY_STORAGE_ITEM_REPLICATOR(TranslationPreset.ENERGY_STORAGE, "Item Replicator"),
    ENERGY_STORAGE_FLUID_REPLICATOR(TranslationPreset.ENERGY_STORAGE, "Fluid Replicator"),
    ENERGY_STORAGE_CHEMICAL_REPLICATOR(TranslationPreset.ENERGY_STORAGE, "Chemical Replicator"),
    ENERGY_STORAGE_LARGE_ROTARY_CONDENSENTRATOR(TranslationPreset.ENERGY_STORAGE, "Large Rotary Condensentrator"),

    //Usage Config
    ENERGY_USAGE_RECYCLER(TranslationPreset.ENERGY_USAGE, "Recycler"),
    ENERGY_USAGE_PLANTING_STATION(TranslationPreset.ENERGY_USAGE, "Planting Station"),
    ENERGY_USAGE_CNC_STAMPER(TranslationPreset.ENERGY_USAGE, "CNC Stamper"),
    ENERGY_USAGE_CNC_LATHE(TranslationPreset.ENERGY_USAGE, "CNC Lathe"),
    ENERGY_USAGE_ROLLING_MILL(TranslationPreset.ENERGY_USAGE, "CNC Rolling Mill"),
    ENERGY_USAGE_AMBIENT_GAS_COLLECTOR(TranslationPreset.ENERGY_USAGE, "Ambient Gas Collector"),
    ENERGY_USAGE_REPLICATOR("usage.replicator", "Replicator", "Settings for configuring Replicator Energy Usage", true),
    ENERGY_USAGE_ITEM_REPLICATOR(TranslationPreset.ENERGY_USAGE, "Item Replicator"),
    ENERGY_USAGE_FLUID_REPLICATOR(TranslationPreset.ENERGY_USAGE, "Fluid Replicator"),
    ENERGY_USAGE_CHEMICAL_REPLICATOR(TranslationPreset.ENERGY_USAGE, "Chemical Replicator"),
    ENERGY_USAGE_LARGE_ROTARY_CONDENSENTRATOR(TranslationPreset.ENERGY_USAGE, "Large Rotary Condensentrator");

    private final String key;
    private final String title;
    private final String tooltip;
    @Nullable
    private final String button;

    MoreMachineConfigTranslations(TranslationPreset preset, String type) {
        this(preset.path(type), preset.title(type), preset.tooltip(type));
    }

    MoreMachineConfigTranslations(TranslationPreset preset, String type, String tooltipSuffix) {
        this(preset.path(type), preset.title(type), preset.tooltip(type) + tooltipSuffix);
    }

    MoreMachineConfigTranslations(String path, String title, String tooltip) {
        this(path, title, tooltip, false);
    }

    MoreMachineConfigTranslations(String path, String title, String tooltip, boolean isSection) {
        this(path, title, tooltip, IConfigTranslation.getSectionTitle(title, isSection));
    }

    MoreMachineConfigTranslations(String path, String title, String tooltip, @Nullable String button) {
        this.key = Util.makeDescriptionId("configuration", Mekmm.rl(path));
        this.title = title;
        this.tooltip = tooltip;
        this.button = button;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return key;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String tooltip() {
        return tooltip;
    }

    @Nullable
    @Override
    public String button() {
        return button;
    }
}
