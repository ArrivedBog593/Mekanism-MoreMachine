package com.jerry.mekmm.common.recipe.impl;

import com.jerry.mekmm.api.recipes.basic.BasicFluidChemicalToFluidRecipe;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

@NothingNullByDefault
public class FluidReplicatorIRecipeSingle extends BasicFluidChemicalToFluidRecipe {
    /**
     * @param fluidInput    Fluid input.
     * @param chemicalInput Chemical input.
     * @param output        Output.
     */
    public FluidReplicatorIRecipeSingle(FluidStackIngredient fluidInput, ChemicalStackIngredient chemicalInput, FluidStack output) {
        super(fluidInput, chemicalInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    @Override
    public String getGroup() {
        return "duplicator";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MoreMachineBlocks.FLUID_REPLICATOR);
    }
}
