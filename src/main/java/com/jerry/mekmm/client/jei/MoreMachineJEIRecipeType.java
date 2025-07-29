package com.jerry.mekmm.client.jei;

import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.client.jei.MekanismJEIRecipeType;

public class MoreMachineJEIRecipeType {

    private MoreMachineJEIRecipeType() {

    }

    public static final MekanismJEIRecipeType<RecyclerRecipe> RECYCLING = new MekanismJEIRecipeType<>(MMBlocks.RECYCLER, RecyclerRecipe.class);

}