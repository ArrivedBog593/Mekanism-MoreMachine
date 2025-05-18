package com.jerry.mekaf.common.tile.factory;

import com.jerry.mekaf.common.inventory.slot.AdvancedFactoryInputInventorySlot;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.PressurizedReactionCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.monitor.FactoryRecipeCacheLookupMonitor;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntBiFunction;

public class TileEntityPressurizedReactingFactory extends TileEntityAdvancedFactoryBase<PressurizedReactionRecipe> implements IHasDumpButton,
        ITripleRecipeLookupHandler.ItemFluidChemicalRecipeLookupHandler<PressurizedReactionRecipe> {

    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_CHEMICAL_INPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEM_INPUT_ERROR,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_CHEMICAL_INPUT_ERROR,
            NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR,
            NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_FLUID_INPUT_ERROR,
            NOT_ENOUGH_CHEMICAL_INPUT_ERROR
    );

    private static final int BASE_DURATION = 5 * SharedConstants.TICKS_PER_SECOND;
    public static final int MAX_FLUID = 10 * FluidType.BUCKET_VOLUME;
    public static final long MAX_GAS = 10L * FluidType.BUCKET_VOLUME;

    private PRCProcessInfo[] processInfoSlots;

    public BasicFluidTank inputFluidTank;
    public IChemicalTank inputChemicalTank;
    public IChemicalTank outputChemicalTank;

    private long recipeEnergyRequired = 0;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IInputHandler<@NotNull ChemicalStack> chemicalInputHandler;

    protected TileEntityPressurizedReactingFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        configComponent.setupInputConfig(TransmissionType.FLUID, inputFluidTank);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, inputChemicalTank);
        configComponent.setupOutputConfig(TransmissionType.CHEMICAL, outputChemicalTank);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.CHEMICAL)
                .setCanTankEject(tank -> tank != inputChemicalTank);

        fluidInputHandler = InputHelper.getInputHandler(inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        chemicalInputHandler = InputHelper.getInputHandler(inputChemicalTank, NOT_ENOUGH_CHEMICAL_INPUT_ERROR);
    }

    @Override
    protected void addTanks(ChemicalTankHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {
        builder.addTank(inputChemicalTank = BasicChemicalTank.createModern(MAX_GAS, ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputChemicalTank),
                ConstantPredicates.alwaysTrueBi(), this::containsRecipeC, ChemicalAttributeValidator.ALWAYS_ALLOW, markAllMonitorsChanged(listener)));
        builder.addTank(outputChemicalTank = BasicChemicalTank.output(MAX_GAS, markAllMonitorsChanged(listener)));
    }

    @Override
    protected @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);
        builder.addTank(inputFluidTank = BasicFluidTank.input(MAX_FLUID, ConstantPredicates.alwaysTrue(),
                this::containsRecipeB, markAllMonitorsChanged(listener)));
        return builder.build();
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {
        itemInputHandlers = new IInputHandler[tier.processes];
        reactionOutputHandlers = new IOutputHandler[tier.processes];
        processInfoSlots = new PRCProcessInfo[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            FactoryRecipeCacheLookupMonitor<PressurizedReactionRecipe> lookupMonitor = recipeCacheLookupMonitors[i];
            IContentsListener updateSortingAndUnpause = () -> {
                updateSortingListener.onContentsChanged();
                lookupMonitor.unpause();
            };
            OutputInventorySlot outputSlot = OutputInventorySlot.at(updateSortingAndUnpause, getXPos(i), 57);
            //Note: As we are an item factory that has comparator's based on items we can just use the monitor as a listener directly
            AdvancedFactoryInputInventorySlot inputSlot = AdvancedFactoryInputInventorySlot.create(this, i, outputSlot, outputChemicalTank, recipeCacheLookupMonitors[i], getXPos(i), 13);
            itemInputHandlers[i] = InputHelper.getInputHandler(inputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
            reactionOutputHandlers[i] = OutputHelper.getOutputHandler(outputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, outputChemicalTank, NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR);
            processInfoSlots[i] = new PRCProcessInfo(i, inputSlot, outputSlot);
        }
    }

    @Override
    public IChemicalTank getChemicalTankBar() {
        return inputChemicalTank;
    }

    public BasicFluidTank getFluidTankBar() {
        return inputFluidTank;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, PressurizedReactionRecipe, InputRecipeCache.ItemFluidChemical<PressurizedReactionRecipe>> getRecipeType() {
        return MekanismRecipeType.REACTION;
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<PressurizedReactionRecipe> recipeViewerType() {
        return RecipeViewerRecipeType.REACTION;
    }

    @Override
    public @Nullable PressurizedReactionRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandlers[cacheIndex], fluidInputHandler, chemicalInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<PressurizedReactionRecipe> createNewCachedRecipe(@NotNull PressurizedReactionRecipe recipe, int cacheIndex) {
        return new PressurizedReactionCachedRecipe(recipe, recheckAllRecipeErrors[cacheIndex], itemInputHandlers[cacheIndex], fluidInputHandler, chemicalInputHandler, reactionOutputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks)
                .setBaselineMaxOperations(this::getOperationsPerTick);
    }

    public boolean inputProducesOutput(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @NotNull IChemicalTank outputTank, boolean updateCache) {
        return outputTank.isEmpty() || getRecipeForInput(process, fallbackInput, outputSlot, outputTank, updateCache) != null;
    }

    @Contract("null, _ -> false")
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<PressurizedReactionRecipe> cached, @NotNull ItemStack stack) {
        if (cached != null) {
            PressurizedReactionRecipe cachedRecipe = cached.getRecipe();
            return cachedRecipe.getInputSolid().testType(stack) &&
                    (inputFluidTank.isEmpty() || cachedRecipe.getInputFluid().testType(inputFluidTank.getFluid())) &&
                    (inputChemicalTank.isEmpty() || cachedRecipe.getInputChemical().testType(inputChemicalTank.getStack()));
        }
        return false;
    }

    @Nullable
    protected PressurizedReactionRecipe getRecipeForInput(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @NotNull IChemicalTank outputTank, boolean updateCache) {
        if (!CommonWorldTickHandler.flushTagAndRecipeCaches) {
            //If our recipe caches are valid, grab our cached recipe and see if it is still valid
            CachedRecipe<PressurizedReactionRecipe> cached = getCachedRecipe(process);
            if (isCachedRecipeValid(cached, fallbackInput)) {
                //Our input matches the recipe we have cached for this slot
                return cached.getRecipe();
            }
        }
        //If there is no cached item input, or it doesn't match our fallback then it is an out of date cache, so we ignore the fact that we have a cache
        PressurizedReactionRecipe foundRecipe = findRecipe(process, fallbackInput, outputSlot, outputTank);
        if (foundRecipe == null) {
            //We could not find any valid recipe for the given item that matches the items in the current output slots
            return null;
        }
        if (updateCache) {
            //If we want to update the cache, then create a new cache with the recipe we found and update the cache
            recipeCacheLookupMonitors[process].updateCachedRecipe(foundRecipe);
        }
        return foundRecipe;
    }

    @Nullable
    protected PressurizedReactionRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, IInventorySlot outputSlot, @NotNull IChemicalTank inputTank) {
        return getRecipeType().getInputCache().findFirstRecipe(level, fallbackInput, inputFluidTank.getFluid(), inputChemicalTank.getStack());
    }

    protected int getNeededInput(PressurizedReactionRecipe recipe, ItemStack inputStack) {
        return MathUtils.clampToInt(recipe.getInputSolid().getNeededAmount(inputStack));
    }

    public boolean isItemValidForSlot(@NotNull ItemStack stack) {
        return containsRecipeABC(stack, inputFluidTank.getFluid(), inputChemicalTank.getStack());
    }

    // 判断输入物品是否符合配方
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return containsRecipeA(stack);
    }

    @Override
    public void dump() {
        inputFluidTank.setStack(FluidStack.EMPTY);
        inputChemicalTank.setEmpty();
    }

    @Override
    protected void sortInventoryOrTank() {

    }

    public record PRCProcessInfo(int process, @NotNull AdvancedFactoryInputInventorySlot inputSlot,
                                 @NotNull IInventorySlot outputItem) {
    }

    protected static class PRCRecipeProcessInfo {

        private final List<PRCProcessInfo> processes = new ArrayList<>();
        @Nullable
        private ToIntBiFunction<PRCRecipeProcessInfo, TileEntityPressurizedReactingFactory> lazyMinPerSlot;
        private Object item;
        private PressurizedReactionRecipe recipe;
        private int minPerSlot = 1;
        private int totalCount;

        public int getMinPerSlot(TileEntityPressurizedReactingFactory factory) {
            if (lazyMinPerSlot != null) {
                //Get the value lazily
                minPerSlot = Math.max(1, lazyMinPerSlot.applyAsInt(this, factory));
                lazyMinPerSlot = null;
            }
            return minPerSlot;
        }
    }
}
