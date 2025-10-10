package com.jerry.mekmm.common.tile.machine;//package com.jerry.mekmm.common.tile.machine;
//
//import com.jerry.mekmm.api.recipes.ChemicalReplicatorRecipe;
//import com.jerry.mekmm.api.recipes.cache.ReplicatorCachedRecipe;
//import com.jerry.mekmm.common.config.MoreMachineConfig;
//import com.jerry.mekmm.common.registries.MMBlocks;
//import com.jerry.mekmm.common.registries.MoreMachineGas;
//import com.jerry.mekmm.common.util.MMUtils;
//import com.jerry.mekmm.common.util.ValidatorUtils;
//import mekanism.api.IContentsListener;
//import mekanism.api.RelativeSide;
//import mekanism.api.chemical.*;
//import mekanism.api.chemical.gas.Gas;
//import mekanism.api.chemical.gas.GasStack;
//import mekanism.api.chemical.gas.IGasTank;
//import mekanism.api.chemical.merged.MergedChemicalTank;
//import mekanism.api.recipes.cache.CachedRecipe;
//import mekanism.api.recipes.chemical.ChemicalChemicalToChemicalRecipe;
//import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
//import mekanism.api.recipes.inputs.BoxedChemicalInputHandler;
//import mekanism.api.recipes.inputs.ILongInputHandler;
//import mekanism.api.recipes.inputs.InputHelper;
//import mekanism.api.recipes.outputs.BoxedChemicalOutputHandler;
//import mekanism.api.recipes.outputs.IOutputHandler;
//import mekanism.api.recipes.outputs.OutputHelper;
//import mekanism.common.capabilities.energy.MachineEnergyContainer;
//import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
//import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
//import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
//import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
//import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
//import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
//import mekanism.common.inventory.container.slot.SlotOverlay;
//import mekanism.common.inventory.slot.EnergyInventorySlot;
//import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
//import mekanism.common.inventory.slot.chemical.GasInventorySlot;
//import mekanism.common.inventory.slot.chemical.MergedChemicalInventorySlot;
//import mekanism.common.lib.transmitter.TransmissionType;
//import mekanism.common.recipe.IMekanismRecipeTypeProvider;
//import mekanism.common.tile.component.TileComponentConfig;
//import mekanism.common.tile.component.TileComponentEjector;
//import mekanism.common.tile.component.config.ConfigInfo;
//import mekanism.common.tile.component.config.DataType;
//import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
//import mekanism.common.tile.prefab.TileEntityProgressMachine;
//import mekanism.common.util.RegistryUtils;
//import net.minecraft.SharedConstants;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Holder;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Objects;
//
//public class TileEntityChemicalReplicator extends TileEntityProgressMachine<ChemicalReplicatorRecipe> {
//
//    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
//            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
//            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
//            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
//            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
//            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
//    );
//
//    public static final long MAX_GAS = 10_000;
//    private static final long MAX_CHEMICAL = 10_000;
//    private static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;
//
//    public static HashMap<String, Integer> customRecipeMap = ValidatorUtils.getRecipeFromConfig(MoreMachineConfig.general.chemicalReplicatorRecipe.get());
//
//    //UU
//    public IGasTank firstInputTank;
//    public MergedChemicalTank secondaryInputTank;
//    public MergedChemicalTank chemicalOutputTank;
//
//    private MachineEnergyContainer<TileEntityChemicalReplicator> energyContainer;
//
//    private final ILongInputHandler<@NotNull GasStack> firstInputHandler;
//    private final BoxedChemicalInputHandler secondaryInputHandler;
//    private final BoxedChemicalOutputHandler outputHandler;
//    //气罐槽
//    //UU物质
//    GasInventorySlot firstSlot;
//    //要复制的化学品
//    MergedChemicalInventorySlot<?> secondarySlot;
//    MergedChemicalInventorySlot<?> outputSlot;
//    EnergyInventorySlot energySlot;
//
//    public TileEntityChemicalReplicator(BlockPos pos, BlockState state) {
//        super(MMBlocks.CHEMICAL_REPLICATOR, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
//        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS, TransmissionType.INFUSION, TransmissionType.PIGMENT,
//                TransmissionType.SLURRY, TransmissionType.ENERGY);
//        configComponent.setupItemIOExtraConfig(firstSlot, outputSlot, secondarySlot, energySlot);
//
//        ConfigInfo gasConfig = configComponent.getConfig(TransmissionType.GAS);
//        if (gasConfig != null) {
//            gasConfig.addSlotInfo(DataType.INPUT_1, new ChemicalSlotInfo.GasSlotInfo(true, false, firstInputTank));
//            gasConfig.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo.GasSlotInfo(true, false, secondaryInputTank.getGasTank()));
//            gasConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.GasSlotInfo(false, true, chemicalOutputTank.getGasTank()));
//        }
//        ConfigInfo infusionConfig = configComponent.getConfig(TransmissionType.INFUSION);
//        if (infusionConfig != null) {
//            infusionConfig.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo.InfusionSlotInfo(true, false, secondaryInputTank.getInfusionTank()));
//            infusionConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.InfusionSlotInfo(false, true, chemicalOutputTank.getInfusionTank()));
//        }
//        ConfigInfo pigmentConfig = configComponent.getConfig(TransmissionType.PIGMENT);
//        if (pigmentConfig != null) {
//            pigmentConfig.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo.PigmentSlotInfo(true, false, secondaryInputTank.getPigmentTank()));
//            pigmentConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.PigmentSlotInfo(false, true, chemicalOutputTank.getPigmentTank()));
//        }
//        ConfigInfo slurryConfig = configComponent.getConfig(TransmissionType.SLURRY);
//        if (slurryConfig != null) {
//            slurryConfig.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo.SlurrySlotInfo(true, false, secondaryInputTank.getSlurryTank()));
//            slurryConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo.SlurrySlotInfo(false, true, chemicalOutputTank.getSlurryTank()));
//        }
//
//        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
//
//        ejectorComponent = new TileComponentEjector(this);
//        ejectorComponent.setOutputData(configComponent, TransmissionType.GAS, TransmissionType.ITEM)
//                .setCanTankEject(tank -> tank == chemicalOutputTank);
//
//        firstInputHandler = InputHelper.getInputHandler(firstInputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
//        secondaryInputHandler = new BoxedChemicalInputHandler(secondaryInputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
//        outputHandler = new BoxedChemicalOutputHandler(chemicalOutputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
//    }
//
//    @Override
//    protected void presetVariables() {
//        super.presetVariables();
//        //TODO: Come up with a better way to grab the listener for this
//        secondaryInputHandler = MergedChemicalTank.create(
//                ChemicalTankBuilder.GAS.input(MAX_CHEMICAL, gas -> getRecipeType().getInputCache().containsInputB(level, gas), getRecipeCacheSaveOnlyListener()),
//                ChemicalTankBuilder.INFUSION.input(MAX_CHEMICAL, infuseType -> getRecipeType().getInputCache().containsInput(level, infuseType), getRecipeCacheSaveOnlyListener()),
//                ChemicalTankBuilder.PIGMENT.input(MAX_CHEMICAL, pigment -> getRecipeType().getInputCache().containsInput(level, pigment), getRecipeCacheSaveOnlyListener()),
//                ChemicalTankBuilder.SLURRY.input(MAX_CHEMICAL, slurry -> getRecipeType().getInputCache().containsInput(level, slurry), getRecipeCacheSaveOnlyListener())
//        );
//    }
//
//    @Override
//    protected @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
//        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
//        builder.addTank(firstInputTank = ChemicalTankBuilder.GAS.create(MAX_GAS, TileEntityChemicalReplicator::isValidGasInput, recipeCacheListener));
//        return builder.build();
//    }
//
//    @Override
//    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
//        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
//        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
//        return builder.build();
//    }
//
//    @Override
//    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
//        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
//        //化学品罐槽位置
//        builder.addSlot(firstSlot = ChemicalInventorySlot.fillOrConvert(firstInputTank, this::getLevel, listener, 29, 65));
//        builder.addSlot(secondarySlot = ChemicalInventorySlot.fillOrConvert(secondaryInputTank, this::getLevel, listener, 8, 65));
//        builder.addSlot(outputSlot = ChemicalInventorySlot.drain(chemicalOutputTank, listener, 132, 65));
//        //能量槽位置
//        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 65));
//        //化学品罐槽减号图标
//        firstSlot.setSlotOverlay(SlotOverlay.MINUS);
//        secondarySlot.setSlotOverlay(SlotOverlay.MINUS);
//        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
//        return builder.build();
//    }
//
//    //需要复制的化学品
//    public static boolean isValidInputChemical(ChemicalStack<?> stack) {
//        if (customRecipeMap != null) {
//            return customRecipeMap.containsKey(Objects.requireNonNull(RegistryUtils.getName(stack.getChemicalHolder())).toString());
//        }
//        return false;
//    }
//
//    //uu物质
//    public static boolean isValidGasInput(Gas gas) {
//        return gas.equals(MoreMachineGas.UU_MATTER.getChemical());
//    }
//
//    @Override
//    protected void onUpdateServer() {
//        super.onUpdateServer();
//        energySlot.fillContainerOrConvert();
//        firstSlot.fillTankOrConvert();
//        secondarySlot.fillTankOrConvert();
//        recipeCacheLookupMonitor.updateAndProcess();
//    }
//
//    public @Nullable MachineEnergyContainer<TileEntityChemicalReplicator> getEnergyContainer() {
//        return energyContainer;
//    }
//
//    @Override
//    public @NotNull IMekanismRecipeTypeProvider<ChemicalReplicatorRecipe, ?> getRecipeType() {
//        return null;
//    }
//
//    @Override
//    public @Nullable ChemicalReplicatorRecipe getRecipe(int cacheIndex) {
//        return getRecipe(firstInputHandler.getInput(), secondaryInputHandler.getInput());
//    }
//
//    @Override
//    public @NotNull CachedRecipe<ChemicalReplicatorRecipe> createNewCachedRecipe(@NotNull ChemicalReplicatorRecipe recipe, int cacheIndex) {
//        return ReplicatorCachedRecipe.createChemicalReplicator(recipe, recheckAllRecipeErrors, firstInputHandler, secondaryInputHandler, outputHandler)
//                .setErrorsChanged(this::onErrorsChanged)
//                .setCanHolderFunction(this::canFunction)
//                .setActive(this::setActive)
//                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
//                .setRequiredTicks(this::getTicksRequired)
//                .setOnFinish(this::markForSave)
//                .setOperatingTicksChanged(this::setOperatingTicks)
//                .setBaselineMaxOperations(this::getOperationsPerTick);
//    }
//
//    @Override
//    public @Nullable IRecipeViewerRecipeType<ChemicalReplicatorRecipe> recipeViewerType() {
//        return MMRecipeViewerRecipeType.CHEMICAL_REPLICATOR;
//    }
//
//    public static ChemicalReplicatorRecipe getRecipe(ChemicalStack chemicalStack, ChemicalStack UUStack) {
//        if (chemicalStack.isEmpty() || UUStack.isEmpty()) {
//            return null;
//        }
//        if (customRecipeMap != null) {
//            Holder<Chemical> chemicalHolder = chemicalStack.getChemicalHolder();
//            //如果为空则赋值为0
//            int amount = customRecipeMap.getOrDefault(RegistryUtils.getName(chemicalHolder).toString(), 0);
//            //防止null和配置文件中出现0
//            if (amount == 0) return null;
//            return new ChemicalReplicatorIRecipeSingle(
//                    IngredientCreatorAccess.chemicalStack().fromHolder(chemicalHolder, 1000),
//                    IngredientCreatorAccess.chemicalStack().fromHolder(MMChemicals.UU_MATTER, amount),
//                    new ChemicalStack(chemicalHolder, 1000)
//            );
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isConfigurationDataCompatible(Block type) {
//        return super.isConfigurationDataCompatible(type) || MMUtils.isSameMMTypeFactory(getBlockHolder(), type);
//    }
//}
