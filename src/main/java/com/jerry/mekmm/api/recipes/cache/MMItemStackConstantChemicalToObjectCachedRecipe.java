package com.jerry.mekmm.api.recipes.cache;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.ItemStackChemicalToObjectRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToObjectCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

public class MMItemStackConstantChemicalToObjectCachedRecipe<OUTPUT, RECIPE extends ItemStackChemicalToObjectRecipe<OUTPUT>> extends ItemStackConstantChemicalToObjectCachedRecipe<OUTPUT, RECIPE> {
    /**
     * @param recipe                   Recipe.
     * @param recheckAllErrors         Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended
     *                                 to not do this every tick or if there is no one viewing recipes.
     * @param itemInputHandler         Item input handler.
     * @param chemicalInputHandler     Chemical input handler.
     * @param chemicalUsage            Chemical usage multiplier.
     * @param chemicalUsedSoFarChanged Called when the number chemical usage so far changes.
     * @param outputHandler            Output handler.
     * @param outputEmptyCheck         Output Empty Check
     */
    public MMItemStackConstantChemicalToObjectCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler, ILongInputHandler<ChemicalStack> chemicalInputHandler, ChemicalUsageMultiplier chemicalUsage, LongConsumer chemicalUsedSoFarChanged, IOutputHandler<@NotNull OUTPUT> outputHandler, Predicate<OUTPUT> outputEmptyCheck) {
        super(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler, chemicalUsage, chemicalUsedSoFarChanged, outputHandler, outputEmptyCheck);
    }

    public static ItemStackConstantChemicalToObjectCachedRecipe<PlantingRecipe.PlantingStationRecipeOutput, PlantingRecipe> planting(PlantingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                                                                                                                     ILongInputHandler<@NotNull ChemicalStack> chemicalInputHandler, ItemStackConstantChemicalToObjectCachedRecipe.ChemicalUsageMultiplier chemicalUsage,
                                                                                                                                     LongConsumer chemicalUsedSoFarChanged, IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler) {
        return new MMItemStackConstantChemicalToObjectCachedRecipe<>(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler,
                chemicalUsage, chemicalUsedSoFarChanged, outputHandler, ConstantPredicates.alwaysFalse());
    }
}
