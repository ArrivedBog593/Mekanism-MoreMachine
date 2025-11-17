package com.jerry.mekmm.api.datagen.recipe.builder;

import com.jerry.mekmm.api.recipes.basic.BasicPlantingRecipe;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

@NothingNullByDefault
public class PlantingStationRecipeBuilder extends MekanismRecipeBuilder<PlantingStationRecipeBuilder> {

    private final ItemStackIngredient itemInput;
    private final ChemicalStackIngredient chemicalInput;
    private final ItemStack mainOutput;
    private final ItemStack secondaryOutput;
    private final boolean perTickUsage;

    protected PlantingStationRecipeBuilder(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack mainOutput, ItemStack secondaryOutput, boolean perTickUsage) {
        this.itemInput = itemInput;
        this.chemicalInput = chemicalInput;
        this.mainOutput = mainOutput;
        this.secondaryOutput = secondaryOutput;
        this.perTickUsage = perTickUsage;
    }

    /**
     * Creates a planting recipe builder.
     * 创建一个种植站的配方生成器。
     *
     * @param itemInput     ItemInput
     * @param chemicalInput ChemicalInput
     * @param mainOutput    MainOutput
     * @param perTickUsage  PerTickUsage
     */
    public static PlantingStationRecipeBuilder planting(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack mainOutput, boolean perTickUsage) {
        if (mainOutput.isEmpty()) {
            throw new IllegalArgumentException("This planting recipe requires a non empty output.");
        }
        return new PlantingStationRecipeBuilder(itemInput, chemicalInput, mainOutput, ItemStack.EMPTY, perTickUsage);
    }

    /**
     * Creates a planting recipe builder.
     * 创建一个种植站的配方生成器。
     *
     * @param itemInput       ItemInput
     * @param chemicalInput   ChemicalInput
     * @param mainOutput      MainOutput
     * @param secondaryOutput SecondaryOutput
     * @param perTickUsage    PerTickUsage
     */
    public static PlantingStationRecipeBuilder planting(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack mainOutput, ItemStack secondaryOutput, boolean perTickUsage) {
        if (mainOutput.isEmpty() || secondaryOutput.isEmpty()) {
            throw new IllegalArgumentException("This planting recipe requires a non empty primary, and secondary output.");
        }
        return new PlantingStationRecipeBuilder(itemInput, chemicalInput, mainOutput, secondaryOutput, perTickUsage);
    }

    @Override
    protected Recipe<?> asRecipe() {
        return new BasicPlantingRecipe(itemInput, chemicalInput, mainOutput, secondaryOutput, perTickUsage);
    }
}
