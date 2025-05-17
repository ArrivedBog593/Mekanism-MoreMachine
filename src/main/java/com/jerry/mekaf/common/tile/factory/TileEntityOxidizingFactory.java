package com.jerry.mekaf.common.tile.factory;

import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.ItemStackToChemicalRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityOxidizingFactory extends TileEntityItemToChemicalAdvancedFactory<ItemStackToChemicalRecipe> implements ISingleRecipeLookupHandler.ItemRecipeLookupHandler<ItemStackToChemicalRecipe> {

    protected static final TriPredicate<ItemStackToChemicalRecipe, ItemStack, ChemicalStack> OUTPUT_CHECK =
            (recipe, input, output) -> ChemicalStack.isSameChemical(recipe.getOutput(input), output);
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY);

    public TileEntityOxidizingFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);

        configComponent.setupItemIOConfig(inputSlots, Collections.emptyList(), energySlot, false);
        configComponent.getConfig(TransmissionType.CHEMICAL).addSlotInfo(DataType.OUTPUT, TileComponentConfig.createInfo(TransmissionType.CHEMICAL, false, true, chemicalTanks));

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.CHEMICAL);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, ItemStackToChemicalRecipe, InputRecipeCache.SingleItem<ItemStackToChemicalRecipe>> getRecipeType() {
        return MekanismRecipeType.OXIDIZING;
    }

    @Override
    public IRecipeViewerRecipeType<ItemStackToChemicalRecipe> recipeViewerType() {
        return RecipeViewerRecipeType.OXIDIZING;
    }

    @Override
    public @Nullable ItemStackToChemicalRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<ItemStackToChemicalRecipe> createNewCachedRecipe(@NotNull ItemStackToChemicalRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.itemToChemical(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex], chemicalOutputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks);
    }

    public boolean inputProducesOutput(int process, @NotNull ItemStack fallbackInput, @NotNull IChemicalTank outputSlot, boolean updateCache) {
        return outputSlot.isEmpty() || getRecipeForInput(process, fallbackInput, outputSlot, updateCache) != null;
    }

    @Contract("null, _ -> false")
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<ItemStackToChemicalRecipe> cached, @NotNull ItemStack stack) {
        return cached != null && cached.getRecipe().getInput().testType(stack);
    }

//    @Nullable
//    protected ItemStackToChemicalRecipe getRecipeForInput(int process, @NotNull ItemStack fallbackInput, @NotNull IChemicalTank outputSlot, boolean updateCache) {
//        if (!CommonWorldTickHandler.flushTagAndRecipeCaches) {
//            //If our recipe caches are valid, grab our cached recipe and see if it is still valid
//            CachedRecipe<ItemStackToChemicalRecipe> cached = getCachedRecipe(process);
//            if (isCachedRecipeValid(cached, fallbackInput)) {
//                //Our input matches the recipe we have cached for this slot
//                return cached.getRecipe();
//            }
//        }
//        //If there is no cached item input, or it doesn't match our fallback then it is an out of date cache, so we ignore the fact that we have a cache
//        ItemStackToChemicalRecipe foundRecipe = findRecipe(process, fallbackInput, outputSlot);
//        if (foundRecipe == null) {
//            //We could not find any valid recipe for the given item that matches the items in the current output slots
//            return null;
//        }
//        if (updateCache) {
//            //If we want to update the cache, then create a new cache with the recipe we found and update the cache
//            recipeCacheLookupMonitors[process].updateCachedRecipe(foundRecipe);
//        }
//        return foundRecipe;
//    }

    @Nullable
    protected ItemStackToChemicalRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IChemicalTank outputSlot) {
        return getRecipeType().getInputCache().findTypeBasedRecipe(level, fallbackInput, outputSlot.getStack(), OUTPUT_CHECK);
    }

    protected int getNeededInput(ItemStackToChemicalRecipe recipe, ItemStack inputStack) {
        return MathUtils.clampToInt(recipe.getInput().getNeededAmount(inputStack));
    }

    @Override
    public IChemicalTank getChemicalTankBar() {
        return null;
    }

    //物品能否放进槽位
    @Override
    public boolean isItemValidForSlot(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return containsRecipe(stack);
    }
}
