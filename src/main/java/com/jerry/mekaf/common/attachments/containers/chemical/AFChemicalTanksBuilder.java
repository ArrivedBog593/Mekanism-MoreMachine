package com.jerry.mekaf.common.attachments.containers.chemical;

import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.attachments.containers.ContainsRecipe;
import mekanism.common.attachments.containers.chemical.AttachedChemicals;
import mekanism.common.attachments.containers.chemical.ComponentBackedChemicalTank;
import mekanism.common.attachments.containers.creator.BaseContainerCreator;
import mekanism.common.attachments.containers.creator.IBasicContainerCreator;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Predicate;

public class AFChemicalTanksBuilder {

    public static AFChemicalTanksBuilder builder() {
        return new AFChemicalTanksBuilder();
    }

    protected final List<IBasicContainerCreator<? extends ComponentBackedChemicalTank>> tankCreators = new ArrayList<>();

    protected AFChemicalTanksBuilder() {
    }

    public BaseContainerCreator<AttachedChemicals, ComponentBackedChemicalTank> build() {
        return new AFBaseChemicalTankBuilder(tankCreators);
    }

    public <VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache> AFChemicalTanksBuilder addBasic(long capacity,
                                                                                                                                                                  IMekanismRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> recipeType, ContainsRecipe<INPUT_CACHE, ChemicalStack> containsRecipe) {
        return addBasic(capacity, chemical -> containsRecipe.check(recipeType.getInputCache(), null, chemical));
    }

    public AFChemicalTanksBuilder addBasic(long capacity, Predicate<ChemicalStack> isValid) {
        return addBasic(() -> capacity, isValid);
    }

    public AFChemicalTanksBuilder addBasic(LongSupplier capacity, Predicate<ChemicalStack> isValid) {
        return addTank((type, attachedTo, containerIndex) -> new ComponentBackedChemicalTank(attachedTo,
                containerIndex, ConstantPredicates.manualOnly(), ConstantPredicates.alwaysTrueBi(), isValid, MekanismConfig.general.chemicalItemFillRate, capacity, null));
    }

    public AFChemicalTanksBuilder addBasic(long capacity) {
        return addBasic(() -> capacity);
    }

    public AFChemicalTanksBuilder addBasic(LongSupplier capacity) {
        return addTank((type, attachedTo, containerIndex) -> new ComponentBackedChemicalTank(attachedTo,
                containerIndex, ConstantPredicates.manualOnly(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue(),
                MekanismConfig.general.chemicalItemFillRate, capacity, null));
    }

    public AFChemicalTanksBuilder addOutputFactoryTank(int process, long capacity) {
        for (int i = 0; i < process; i++) {
            addBasic(capacity);
        }
        return this;
    }

    public AFChemicalTanksBuilder addInputFactoryTank(int process, long capacity, Predicate<ChemicalStack> recipeInputPredicate) {
        IBasicContainerCreator<ComponentBackedChemicalTank> inputTankCreator = (type, attachedTo, containerIndex) -> new ComponentBackedChemicalTank(attachedTo,
                containerIndex, ConstantPredicates.manualOnly(), ConstantPredicates.alwaysTrueBi(), recipeInputPredicate, MekanismConfig.general.chemicalItemFillRate, () -> capacity, null);
        for (int i = 0; i < process; i++) {
            addTank(inputTankCreator);
        }
        return this;
    }

    public AFChemicalTanksBuilder addInternalStorage(LongSupplier rate, LongSupplier capacity, Predicate<ChemicalStack> isValid) {
        return addTank((type, attachedTo, containerIndex) -> new ComponentBackedChemicalTank(attachedTo,
                containerIndex, ConstantPredicates.notExternal(), ConstantPredicates.alwaysTrueBi(), isValid, rate, capacity, null));
    }

    public AFChemicalTanksBuilder addTank(IBasicContainerCreator<? extends ComponentBackedChemicalTank> tank) {
        tankCreators.add(tank);
        return this;
    }

    private static class AFBaseChemicalTankBuilder extends BaseContainerCreator<AttachedChemicals, ComponentBackedChemicalTank> {

        public AFBaseChemicalTankBuilder(List<IBasicContainerCreator<? extends ComponentBackedChemicalTank>> creators) {
            super(creators);
        }

        @Override
        public AttachedChemicals initStorage(int containers) {
            return AttachedChemicals.create(containers);
        }
    }
}
