package com.jerry.mekmm.common.tile.factory;

import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.api.recipes.cache.ReplicatorCachedRecipe;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.recipe.impl.ReplicatorIRecipeSingle;
import com.jerry.mekmm.common.registries.MMChemicals;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.upgrade.AdvancedMachineUpgradeData;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.RegistryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityReplicatingFactory extends MMTileEntityItemToItemFactory<MMBasicItemStackChemicalToItemStackRecipe> implements IHasDumpButton,
        IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<MMBasicItemStackChemicalToItemStackRecipe> {

    protected static final DoubleInputRecipeCache.CheckRecipeType<ItemStack, ChemicalStack, MMBasicItemStackChemicalToItemStackRecipe, ItemStack> OUTPUT_CHECK =
            (recipe, input, extra, output) -> InventoryUtils.areItemsStackable(recipe.getOutput(input, extra), output);
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT
    );

    public static final long MAX_GAS = 10 * FluidType.BUCKET_VOLUME;

    public static HashMap<String, Integer> customRecipeMap = getRecipeFromConfig();

    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;
    //化学品存储槽
    public IChemicalTank chemicalTank;
    //气罐槽
    ChemicalInventorySlot chemicalSlot;

    public TileEntityReplicatingFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, chemicalTank);

        chemicalInputHandler = InputHelper.getConstantInputHandler(chemicalTank);
    }

    public static HashMap<String, Integer> getRecipeFromConfig() {
        HashMap<String, Integer> map = new HashMap<>();
        List<?> pre = MMConfig.general.duplicatorRecipe.get();
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

    public IChemicalTank getChemicalTank() {
        return chemicalTank;
    }

    @Nullable
    @Override
    public ChemicalInventorySlot getExtraSlot() {
        return chemicalSlot;
    }

    @Override
    public @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        chemicalTank = BasicChemicalTank.inputModern(MAX_GAS * tier.processes, TileEntityReplicatingFactory::isValidChemicalInput, markAllMonitorsChanged(listener));
        builder.addTank(chemicalTank);
        return builder.build();
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {
        super.addSlots(builder, listener, updateSortingListener);
        builder.addSlot(chemicalSlot = ChemicalInventorySlot.fillOrConvert(chemicalTank, this::getLevel, listener, 7, 57));
    }

    @Override
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<MMBasicItemStackChemicalToItemStackRecipe> cached, @NotNull ItemStack stack) {
        return cached != null && isValidChemicalInput(chemicalTank.getStack());
    }

    @Override
    protected @Nullable MMBasicItemStackChemicalToItemStackRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot) {
        return null;
    }

    @Override
    protected void handleSecondaryFuel() {
        chemicalSlot.fillTankOrConvert();
    }

    @Override
    protected int getNeededInput(MMBasicItemStackChemicalToItemStackRecipe recipe, ItemStack inputStack) {
        return MathUtils.clampToInt(recipe.getItemInput().getNeededAmount(inputStack));
    }

    @Override
    public boolean isItemValidForSlot(@NotNull ItemStack stack) {
        return true;
    }

    public static boolean isValidChemicalInput(ChemicalStack stack) {
        return stack.is(MMChemicals.UU_MATTER);
    }

    @Override
    public boolean isValidInputItem(ItemStack stack) {
        Item item = stack.getItem();
        if (customRecipeMap != null) {
            return customRecipeMap.containsKey(Objects.requireNonNull(RegistryUtils.getName(item.builtInRegistryHolder())).toString());
        }
        return false;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, MMBasicItemStackChemicalToItemStackRecipe, InputRecipeCache.ItemChemical<MMBasicItemStackChemicalToItemStackRecipe>> getRecipeType() {
        return null;
    }

    @Override
    public @Nullable MMBasicItemStackChemicalToItemStackRecipe getRecipe(int cacheIndex) {
        return getRecipe(inputHandlers[cacheIndex].getInput(), chemicalInputHandler.getInput());
    }

    @Override
    public @NotNull CachedRecipe<MMBasicItemStackChemicalToItemStackRecipe> createNewCachedRecipe(@NotNull MMBasicItemStackChemicalToItemStackRecipe recipe, int cacheIndex) {
        return ReplicatorCachedRecipe.createCache(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex], chemicalInputHandler, outputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks)
                .setBaselineMaxOperations(this::getOperationsPerTick);
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<MMBasicItemStackChemicalToItemStackRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.REPLICATOR;
    }

    public static MMBasicItemStackChemicalToItemStackRecipe getRecipe(ItemStack itemStack, ChemicalStack chemicalStack) {
        if (chemicalStack.isEmpty() || itemStack.isEmpty()) {
            return null;
        }
        if (customRecipeMap != null) {
            Item item = itemStack.getItem();
            //如果为空则赋值为0
            int amount = customRecipeMap.getOrDefault(RegistryUtils.getName(itemStack.getItemHolder()).toString(), 0);
            //防止null和配置文件中出现0
            if (amount == 0) return null;
            return new ReplicatorIRecipeSingle(
                    IngredientCreatorAccess.item().from(item, 1),
                    IngredientCreatorAccess.chemicalStack().fromHolder(MMChemicals.UU_MATTER, amount),
                    new ItemStack(item, 1)
            );
        }
        return null;
    }

    @Override
    public boolean hasSecondaryResourceBar() {
        return true;
    }

    @Override
    public void parseUpgradeData(HolderLookup.Provider provider, @NotNull IUpgradeData upgradeData) {
        if (upgradeData instanceof AdvancedMachineUpgradeData data) {
            //Generic factory upgrade data handling
            super.parseUpgradeData(provider, upgradeData);
            //Copy the contents using NBT so that if it is not actually valid due to a reload we don't crash
            chemicalTank.deserializeNBT(provider, data.stored.serializeNBT(provider));
            chemicalSlot.deserializeNBT(provider, data.chemicalSlot.serializeNBT(provider));
        } else {
            Mekanism.logger.warn("Unhandled upgrade data.", new Throwable());
        }
    }

    @Override
    public @Nullable AdvancedMachineUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return new AdvancedMachineUpgradeData(provider, redstone, getControlType(), getEnergyContainer(), progress, null, chemicalTank, chemicalSlot, energySlot,
                inputSlots, outputSlots, isSorting(), getComponents());
    }

    @Override
    public void dump() {
        chemicalTank.setEmpty();
    }
}
