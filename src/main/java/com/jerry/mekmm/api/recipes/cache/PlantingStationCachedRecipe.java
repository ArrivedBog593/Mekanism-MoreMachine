package com.jerry.mekmm.api.recipes.cache;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.PlantingRecipe.PlantingStationRecipeOutput;

import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.ItemStackChemicalToObjectRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToObjectCachedRecipe.ChemicalUsageMultiplier;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

public class PlantingStationCachedRecipe<OUTPUT, RECIPE extends ItemStackChemicalToObjectRecipe<OUTPUT>> extends MMItemStackConstantChemicalToObjectCachedRecipe<OUTPUT, RECIPE> {

    /**
     * @param recipe                   Recipe.
     * @param recheckAllErrors         Returns {@code true} if processing should be continued even if an error is hit in
     *                                 order to gather all the errors. It is recommended
     *                                 to not do this every tick or if there is no one viewing recipes.
     * @param itemInputHandler         Item input handler.
     * @param chemicalInputHandler     Chemical input handler.
     * @param chemicalUsage            Chemical usage multiplier.
     * @param chemicalUsedSoFarChanged Called when the number chemical usage so far changes.
     * @param outputHandler            Output handler.
     * @param outputEmptyCheck         Output Empty Check
     */
    public PlantingStationCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler, ILongInputHandler<ChemicalStack> chemicalInputHandler, ChemicalUsageMultiplier chemicalUsage, LongConsumer chemicalUsedSoFarChanged, IOutputHandler<@NotNull OUTPUT> outputHandler, Predicate<OUTPUT> outputEmptyCheck) {
        super(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler, chemicalUsage, chemicalUsedSoFarChanged, outputHandler, outputEmptyCheck);
    }

    public static MMItemStackConstantChemicalToObjectCachedRecipe<PlantingStationRecipeOutput, PlantingRecipe> planting(PlantingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                                                                                                        ILongInputHandler<@NotNull ChemicalStack> chemicalInputHandler, ChemicalUsageMultiplier chemicalUsage,
                                                                                                                        LongConsumer chemicalUsedSoFarChanged, IOutputHandler<PlantingStationRecipeOutput> outputHandler) {
        return new PlantingStationCachedRecipe<>(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler,
                chemicalUsage, chemicalUsedSoFarChanged, outputHandler, ConstantPredicates.alwaysFalse());
    }

    @Override
    protected void finishProcessing(int operations) {
        if (recipeChemical != null && output != null && !recipeItem.isEmpty() && !recipeChemical.isEmpty() && !outputEmptyCheck.test(output)) {
            if (chemicalUsageMultiplier > 0) {
                chemicalInputHandler.use(recipeChemical, operations * chemicalUsageMultiplier);
            }
            outputHandler.handleOutput(output, operations);
        }
    }
}
