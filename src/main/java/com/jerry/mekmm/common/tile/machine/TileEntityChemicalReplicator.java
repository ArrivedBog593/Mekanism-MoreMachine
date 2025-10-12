package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.api.recipes.basic.MMBasicChemicalChemicalToChemicalRecipe;
import com.jerry.mekmm.api.recipes.cache.ReplicatorCachedRecipe;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.recipe.impl.ChemicalReplicatorIRecipeSingle;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.registries.MoreMachineChemicals;
import com.jerry.mekmm.common.util.MoreMachineUtils;
import com.jerry.mekmm.common.util.ValidatorUtils;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.RegistryUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityChemicalReplicator extends TileEntityProgressMachine<MMBasicChemicalChemicalToChemicalRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    public static final long MAX_GAS = 10 * FluidType.BUCKET_VOLUME;
    private static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;

    public static HashMap<String, Integer> customRecipeMap = ValidatorUtils.getRecipeFromConfig(MoreMachineConfig.general.chemicalReplicatorRecipe.get());

    public IChemicalTank firstInputTank;
    public IChemicalTank chemicalOutputTank;
    //UU
    public IChemicalTank secondaryInputTank;

    private MachineEnergyContainer<TileEntityChemicalReplicator> energyContainer;

    private final ILongInputHandler<@NotNull ChemicalStack> firstInputHandler;
    private final ILongInputHandler<ChemicalStack> secondaryInputHandler;
    private final IOutputHandler<@NotNull ChemicalStack> outputHandler;
    //气罐槽
    //UU物质
    ChemicalInventorySlot firstSlot;
    //要复制的化学品
    ChemicalInventorySlot secondarySlot;
    ChemicalInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public TileEntityChemicalReplicator(BlockPos pos, BlockState state) {
        super(MoreMachineBlocks.CHEMICAL_REPLICATOR, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
        configComponent.setupItemIOExtraConfig(firstSlot, outputSlot, secondarySlot, energySlot);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.CHEMICAL);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT_1, new ChemicalSlotInfo(true, false, firstInputTank));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo(true, false, secondaryInputTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo(false, true, chemicalOutputTank));
        }
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.CHEMICAL, TransmissionType.ITEM)
                .setCanTankEject(tank -> tank == chemicalOutputTank);

        firstInputHandler = InputHelper.getInputHandler(firstInputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(chemicalOutputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        secondaryInputHandler = InputHelper.getConstantInputHandler(secondaryInputTank);
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(firstInputTank = BasicChemicalTank.inputModern(MAX_GAS, TileEntityChemicalReplicator::isValidInputChemical, recipeCacheListener));
        builder.addTank(secondaryInputTank = BasicChemicalTank.inputModern(MAX_GAS, TileEntityChemicalReplicator::isValidChemicalInput, recipeCacheListener));
        builder.addTank(chemicalOutputTank = BasicChemicalTank.output(MAX_GAS, recipeCacheUnpauseListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, recipeCacheUnpauseListener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        //化学品罐槽位置
        builder.addSlot(firstSlot = ChemicalInventorySlot.fillOrConvert(firstInputTank, this::getLevel, listener, 29, 65));
        builder.addSlot(secondarySlot = ChemicalInventorySlot.fillOrConvert(secondaryInputTank, this::getLevel, listener, 8, 65));
        builder.addSlot(outputSlot = ChemicalInventorySlot.drain(chemicalOutputTank, listener, 132, 65));
        //能量槽位置
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 65));
        //化学品罐槽减号图标
        firstSlot.setSlotOverlay(SlotOverlay.MINUS);
        secondarySlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    //需要复制的化学品
    public static boolean isValidInputChemical(ChemicalStack stack) {
        if (customRecipeMap != null) {
            return customRecipeMap.containsKey(Objects.requireNonNull(RegistryUtils.getName(stack.getChemicalHolder())).toString());
        }
        return false;
    }

    //uu物质
    public static boolean isValidChemicalInput(ChemicalStack stack) {
        return stack.is(MoreMachineChemicals.UU_MATTER);
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        firstSlot.fillTankOrConvert();
        secondarySlot.fillTankOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        return sendUpdatePacket;
    }

    public @Nullable MachineEnergyContainer<TileEntityChemicalReplicator> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, MMBasicChemicalChemicalToChemicalRecipe, ?> getRecipeType() {
        return null;
    }

    @Override
    public @Nullable MMBasicChemicalChemicalToChemicalRecipe getRecipe(int cacheIndex) {
        return getRecipe(firstInputHandler.getInput(), secondaryInputHandler.getInput());
    }

    @Override
    public @NotNull CachedRecipe<MMBasicChemicalChemicalToChemicalRecipe> createNewCachedRecipe(@NotNull MMBasicChemicalChemicalToChemicalRecipe recipe, int cacheIndex) {
        return ReplicatorCachedRecipe.createChemicalReplicator(recipe, recheckAllRecipeErrors, firstInputHandler, secondaryInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks)
                .setBaselineMaxOperations(this::getOperationsPerTick);
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<MMBasicChemicalChemicalToChemicalRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.CHEMICAL_REPLICATOR;
    }

    public static MMBasicChemicalChemicalToChemicalRecipe getRecipe(ChemicalStack chemicalStack, ChemicalStack UUStack) {
        if (chemicalStack.isEmpty() || UUStack.isEmpty()) {
            return null;
        }
        if (customRecipeMap != null) {
            Holder<Chemical> chemicalHolder = chemicalStack.getChemicalHolder();
            //如果为空则赋值为0
            int amount = customRecipeMap.getOrDefault(RegistryUtils.getName(chemicalHolder).toString(), 0);
            //防止null和配置文件中出现0
            if (amount == 0) return null;
            return new ChemicalReplicatorIRecipeSingle(
                    IngredientCreatorAccess.chemicalStack().fromHolder(chemicalHolder, 1000),
                    IngredientCreatorAccess.chemicalStack().fromHolder(MoreMachineChemicals.UU_MATTER, amount),
                    new ChemicalStack(chemicalHolder, 1000)
            );
        }
        return null;
    }

    @Override
    public boolean isConfigurationDataCompatible(Block type) {
        return super.isConfigurationDataCompatible(type) || MoreMachineUtils.isSameMMTypeFactory(getBlockHolder(), type);
    }
}
