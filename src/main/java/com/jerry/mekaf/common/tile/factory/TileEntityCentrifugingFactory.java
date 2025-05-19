package com.jerry.mekaf.common.tile.factory;

import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.ChemicalToChemicalRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.monitor.FactoryRecipeCacheLookupMonitor;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IBoundingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TileEntityCentrifugingFactory extends TileEntityChemicalToChemicalAdvancedFactory<ChemicalToChemicalRecipe> implements IBoundingBlock, ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler<ChemicalToChemicalRecipe> {

    protected static final TriPredicate<ChemicalToChemicalRecipe, ChemicalStack, ChemicalStack> OUTPUT_CHECK =
            (recipe, input, output) -> ChemicalStack.isSameChemical(recipe.getOutput(input), output);
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY);

    private long clientEnergyUsed = 0L;
    private int baselineMaxOperations = 1;

    public TileEntityCentrifugingFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.CHEMICAL)
                .setCanTankEject(tank -> !inputChemicalTanks.contains(tank));
    }

    @Override
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<ChemicalToChemicalRecipe> cached, @NotNull ChemicalStack stack) {
        return cached != null && cached.getRecipe().getInput().testType(stack);
    }

    @Override
    protected @Nullable ChemicalToChemicalRecipe findRecipe(int process, @NotNull ChemicalStack fallbackInput, @NotNull IChemicalTank outputSlot) {
        return getRecipeType().getInputCache().findTypeBasedRecipe(level, fallbackInput, outputSlot.getStack(), OUTPUT_CHECK);
    }

    @Override
    public boolean isChemicalValidForTank(@NotNull ChemicalStack stack) {
        return containsRecipe(stack);
    }

    @Override
    public boolean isValidInputChemical(@NotNull ChemicalStack stack) {
        return containsRecipe(stack);
    }

    @Override
    protected int getNeededInput(ChemicalToChemicalRecipe recipe, ChemicalStack inputStack) {
        return MathUtils.clampToInt(recipe.getInput().getNeededAmount(inputStack));
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {

    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        for (FactoryRecipeCacheLookupMonitor<ChemicalToChemicalRecipe> recipeCacheLookupMonitor : recipeCacheLookupMonitors) {
            clientEnergyUsed += recipeCacheLookupMonitor.updateAndProcess(energyContainer);
        }
        return sendUpdatePacket;
    }

    public long getEnergyUsed() {
        return clientEnergyUsed;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, ChemicalToChemicalRecipe, InputRecipeCache.SingleChemical<ChemicalToChemicalRecipe>> getRecipeType() {
        return MekanismRecipeType.CENTRIFUGING;
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<ChemicalToChemicalRecipe> recipeViewerType() {
        return RecipeViewerRecipeType.CENTRIFUGING;
    }

    @Override
    public @Nullable ChemicalToChemicalRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(chemicalInputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<ChemicalToChemicalRecipe> createNewCachedRecipe(@NotNull ChemicalToChemicalRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.chemicalToChemical(recipe, recheckAllRecipeErrors[cacheIndex], chemicalInputHandlers[cacheIndex], chemicalOutputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(() -> baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            baselineMaxOperations = (int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED));
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
    }
}
