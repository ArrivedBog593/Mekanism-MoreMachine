package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.api.recipes.StamperRecipe;
import com.jerry.mekmm.api.recipes.cache.StamperCachedRecipe;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.upgrade.StamperUpgradeData;
import com.jerry.mekmm.common.util.MoreMachineUtils;

import mekanism.api.IContentsListener;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.DoubleItem;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityStamper extends TileEntityProgressMachine<StamperRecipe> implements IDoubleRecipeLookupHandler.DoubleItemRecipeLookupHandler<StamperRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    public static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;

    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;
    private final IInputHandler<@NotNull ItemStack> extraInputHandler;

    private MachineEnergyContainer<TileEntityStamper> energyContainer;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getMainInput", docPlaceholder = "main input slot")
    InputInventorySlot mainInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getSecondaryInput", docPlaceholder = "secondary input slot")
    InputInventorySlot extraInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutput", docPlaceholder = "output slot")
    OutputInventorySlot outputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public TileEntityStamper(BlockPos pos, BlockState state) {
        super(MoreMachineBlocks.CNC_STAMPER, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
        configComponent.setupItemIOExtraConfig(mainInputSlot, outputSlot, extraInputSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(mainInputSlot, RecipeError.NOT_ENOUGH_INPUT);
        extraInputHandler = InputHelper.getInputHandler(extraInputSlot, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, recipeCacheUnpauseListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(mainInputSlot = InputInventorySlot.at(item -> containsRecipeAB(item, extraInputSlot.getStack()), this::containsRecipeA, recipeCacheListener,
                64, 17)).tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(extraInputSlot = InputInventorySlot.at(item -> containsRecipeBA(mainInputSlot.getStack(), item), this::containsRecipeB, recipeCacheListener,
                64, 53)).tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(recipeCacheUnpauseListener, 116, 35))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 39, 35));
        extraInputSlot.setSlotType(ContainerSlotType.EXTRA);
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        return sendUpdatePacket;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, StamperRecipe, DoubleItem<StamperRecipe>> getRecipeType() {
        return MoreMachineRecipeType.STAMPING;
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<StamperRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.STAMPING;
    }

    @Nullable
    @Override
    public StamperRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler, extraInputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<StamperRecipe> createNewCachedRecipe(@NotNull StamperRecipe recipe, int cacheIndex) {
        return StamperCachedRecipe.createCache(recipe, recheckAllRecipeErrors, inputHandler, extraInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks)
                .setBaselineMaxOperations(this::getOperationsPerTick);
    }

    @NotNull
    @Override
    public StamperUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return new StamperUpgradeData(provider, redstone, getControlType(), getEnergyContainer(), getOperatingTicks(), energySlot, extraInputSlot, mainInputSlot, outputSlot, getComponents());
    }

    public MachineEnergyContainer<TileEntityStamper> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public boolean isConfigurationDataCompatible(Block type) {
        return super.isConfigurationDataCompatible(type) || MoreMachineUtils.isSameMMTypeFactory(getBlockHolder(), type);
    }

    // Methods relating to IComputerTile
    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    long getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : 0L;
    }
    // End methods IComputerTile
}
