package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.api.recipes.basic.BasicFluidChemicalToFluidRecipe;
import com.jerry.mekmm.api.recipes.cache.ReplicatorCachedRecipe;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.recipe.impl.FluidReplicatorIRecipeSingle;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.registries.MMChemicals;
import com.jerry.mekmm.common.util.MMUtils;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.RegistryUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityFluidReplicator extends TileEntityProgressMachine<BasicFluidChemicalToFluidRecipe> {

    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    public static final int MAX_FLUID = 10 * FluidType.BUCKET_VOLUME;
    public static final long MAX_GAS = 10 * FluidType.BUCKET_VOLUME;
    private static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;

    public static HashMap<String, Integer> customRecipeMap = getRecipeFromConfig();

    public BasicFluidTank fluidInputTank;
    public BasicFluidTank fluidOutputTank;
    //化学品存储槽
    public IChemicalTank chemicalTank;

    private MachineEnergyContainer<TileEntityFluidReplicator> energyContainer;

    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final IOutputHandler<@NotNull FluidStack> fluidOutputHandler;
    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;

    FluidInventorySlot lFluidInputSlot;
    FluidInventorySlot rFluidInputSlot;
    //流体储罐输入输出物品槽
    FluidInventorySlot fluidInputSlot;
    OutputInventorySlot fluidOutputSlot;
    //气罐槽
    ChemicalInventorySlot chemicalSlot;
    EnergyInventorySlot energySlot;

    public TileEntityFluidReplicator(BlockPos pos, BlockState state) {
        super(MMBlocks.FLUID_REPLICATOR, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
//        configComponent.setupItemIOExtraConfig(fluidInputSlot, fluidOutputSlot, chemicalSlot, energySlot);
        configComponent.setupItemIOConfig(List.of(fluidInputSlot, lFluidInputSlot, rFluidInputSlot), Collections.singletonList(fluidOutputSlot), energySlot, false);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, true, chemicalSlot));
        }
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, fluidInputTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, fluidOutputTank));
        }
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, chemicalTank);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID, TransmissionType.ITEM);

        fluidInputHandler = InputHelper.getInputHandler(fluidInputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        fluidOutputHandler = OutputHelper.getOutputHandler(fluidOutputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        chemicalInputHandler = InputHelper.getConstantInputHandler(chemicalTank);
    }

    public static HashMap<String, Integer> getRecipeFromConfig() {
        HashMap<String, Integer> map = new HashMap<>();
        List<?> pre = MMConfig.general.fluidDuplicatorRecipe.get();
        List<String> recipes = new ArrayList<>();
        for (Object item : pre) {
            if (item instanceof String list) {
                recipes.add(list);
            }
        }
        if (recipes.isEmpty()) return null;
        for (String element : recipes) {
            String[] parts = element.split("#", 2); // 分割成最多两部分
            if (parts.length != 2) continue;

            String key = parts[0];
            int value = Integer.parseInt(parts[1]);
            map.put(key, value);
        }
        return map;
    }

    @Override
    protected @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);
        builder.addTank(fluidInputTank = BasicFluidTank.input(FluidType.BUCKET_VOLUME, TileEntityFluidReplicator::isValidFluidInput, recipeCacheListener));
        builder.addTank(fluidOutputTank = BasicFluidTank.output(MAX_FLUID, recipeCacheUnpauseListener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(chemicalTank = BasicChemicalTank.inputModern(MAX_GAS, TileEntityFluidReplicator::isValidChemicalInput, recipeCacheListener));
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
        //输入
        builder.addSlot(fluidInputSlot = FluidInventorySlot.fill(fluidInputTank, listener, 180, 71));
        builder.addSlot(fluidOutputSlot = OutputInventorySlot.at(listener, 180, 102));
        //输出
        builder.addSlot(lFluidInputSlot = FluidInventorySlot.drain(fluidInputTank, listener, 29, 65));
        builder.addSlot(rFluidInputSlot = FluidInventorySlot.drain(fluidOutputTank, listener, 132, 65));
        //化学品罐槽位置
        builder.addSlot(chemicalSlot = ChemicalInventorySlot.fillOrConvert(chemicalTank, this::getLevel, listener, 8, 65));
        //能量槽位置
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 65));
        //化学品罐槽减号图标
        chemicalSlot.setSlotOverlay(SlotOverlay.MINUS);
        fluidInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        lFluidInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        rFluidInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    public static boolean isValidFluidInput(FluidStack stack) {
        Fluid fluid = stack.getFluid();
        if (customRecipeMap != null) {
            return customRecipeMap.containsKey(Objects.requireNonNull(RegistryUtils.getName(fluid.builtInRegistryHolder())).toString());
        }
        return false;
    }

    public static boolean isValidChemicalInput(ChemicalStack stack) {
        return stack.is(MMChemicals.UU_MATTER);
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        fluidInputSlot.fillTank(fluidOutputSlot);
        chemicalSlot.fillTankOrConvert();
        lFluidInputSlot.drainTank(fluidOutputSlot);
        rFluidInputSlot.drainTank(fluidOutputSlot);
        recipeCacheLookupMonitor.updateAndProcess();
        return sendUpdatePacket;
    }

    public @Nullable MachineEnergyContainer<TileEntityFluidReplicator> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, BasicFluidChemicalToFluidRecipe, ?> getRecipeType() {
        return null;
    }

    @Override
    public @Nullable BasicFluidChemicalToFluidRecipe getRecipe(int cacheIndex) {
        return getRecipe(fluidInputHandler.getInput(), chemicalInputHandler.getInput());
    }

    @Override
    public @NotNull CachedRecipe<BasicFluidChemicalToFluidRecipe> createNewCachedRecipe(@NotNull BasicFluidChemicalToFluidRecipe recipe, int cacheIndex) {
        return ReplicatorCachedRecipe.createFluidReplicator(recipe, recheckAllRecipeErrors, fluidInputHandler, chemicalInputHandler, fluidOutputHandler)
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
    public @Nullable IRecipeViewerRecipeType<BasicFluidChemicalToFluidRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.FLUID_REPLICATOR;
    }

    public static BasicFluidChemicalToFluidRecipe getRecipe(FluidStack fluidStack, ChemicalStack chemicalStack) {
        if (chemicalStack.isEmpty() || fluidStack.isEmpty()) {
            return null;
        }
        if (customRecipeMap != null) {
            Fluid fluid = fluidStack.getFluid();
            //如果为空则赋值为0
            int amount = customRecipeMap.getOrDefault(RegistryUtils.getName(fluidStack.getFluidHolder()).toString(), 0);
            //防止null和配置文件中出现0
            if (amount == 0) return null;
            return new FluidReplicatorIRecipeSingle(
                    IngredientCreatorAccess.fluid().fromHolder(fluid.builtInRegistryHolder(), 1),
                    IngredientCreatorAccess.chemicalStack().fromHolder(MMChemicals.UU_MATTER, amount),
                    new FluidStack(fluid, FluidType.BUCKET_VOLUME)
            );
        }
        return null;
    }

    @Override
    public boolean isConfigurationDataCompatible(Block type) {
        return super.isConfigurationDataCompatible(type) || MMUtils.isSameMMTypeFactory(getBlockHolder(), type);
    }
}
