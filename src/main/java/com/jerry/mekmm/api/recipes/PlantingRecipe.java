package com.jerry.mekmm.api.recipes;

import com.jerry.mekmm.Mekmm;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackChemicalToObjectRecipe;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a recipe that can be used in the Planting Station.
 *
 * @author Jerry
 */
@NothingNullByDefault
public abstract class PlantingRecipe extends ItemStackChemicalToObjectRecipe<PlantingRecipe.PlantingStationRecipeOutput> {

    private static final Holder<Item> PLANTING_STATION = DeferredHolder.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "plating_station"));

    @Override
    public RecipeType<?> getType() {
        return MoreMachineRecipeTypes.TYPE_PLANTING.value();
    }

    @Override
    public String getGroup() {
        return "planting_station";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(PLANTING_STATION);
    }

    /**
     * @apiNote Main item cannot be null, secondary item can be null.
     */
    public record PlantingStationRecipeOutput(@NotNull ItemStack first, @NotNull ItemStack second) {

        public PlantingStationRecipeOutput {
            Objects.requireNonNull(first, "First output cannot be null.");
            if (first.isEmpty()) {
                throw new IllegalArgumentException("First output cannot be null");
            }
        }
    }
}
