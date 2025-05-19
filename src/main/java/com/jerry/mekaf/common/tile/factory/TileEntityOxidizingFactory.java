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
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
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

        ConfigInfo chemicalConfig = configComponent.getConfig(TransmissionType.CHEMICAL);
        if (chemicalConfig != null) {
            chemicalConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo(false, true, chemicalTanks));
        }

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
        return findFirstRecipe(itemInputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<ItemStackToChemicalRecipe> createNewCachedRecipe(@NotNull ItemStackToChemicalRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.itemToChemical(recipe, recheckAllRecipeErrors[cacheIndex], itemInputHandlers[cacheIndex], chemicalOutputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks);
    }

    @Contract("null, _ -> false")
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<ItemStackToChemicalRecipe> cached, @NotNull ItemStack stack) {
        return cached != null && cached.getRecipe().getInput().testType(stack);
    }

    @Nullable
    protected ItemStackToChemicalRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IChemicalTank outputSlot) {
        return getRecipeType().getInputCache().findTypeBasedRecipe(level, fallbackInput, outputSlot.getStack(), OUTPUT_CHECK);
    }

    protected int getNeededInput(ItemStackToChemicalRecipe recipe, ItemStack inputStack) {
        return MathUtils.clampToInt(recipe.getInput().getNeededAmount(inputStack));
    }

    //物品能否放进槽位
    @Override
    public boolean isItemValidForSlot(@NotNull ItemStack stack) {
        return containsRecipe(stack);
    }

    @Override
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return containsRecipe(stack);
    }
}
