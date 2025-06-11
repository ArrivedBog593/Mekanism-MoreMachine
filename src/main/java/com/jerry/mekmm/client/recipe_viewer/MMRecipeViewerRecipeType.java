package com.jerry.mekmm.client.recipe_viewer;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.api.recipes.StamperRecipe;
import com.jerry.mekmm.api.recipes.basic.BasicFluidChemicalToFluidRecipe;
import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.recipe_viewer.type.FakeRVRecipeType;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;

public class MMRecipeViewerRecipeType {

    private MMRecipeViewerRecipeType() {

    }

    public static final RVRecipeTypeWrapper<?, RecyclerRecipe, ?> RECYCLER = new RVRecipeTypeWrapper<>(MoreMachineRecipeType.RECYCLING, RecyclerRecipe.class, -28, -16, 144, 54, MMBlocks.RECYCLER);

    public static final RVRecipeTypeWrapper<?, PlantingRecipe, ?> PLANTING_STATION = new RVRecipeTypeWrapper<>(MoreMachineRecipeType.PLANTING_STATION, PlantingRecipe.class, -28, -16, 144, 54, MMBlocks.PLANTING_STATION);

    public static final RVRecipeTypeWrapper<?, StamperRecipe, ?> STAMPING = new RVRecipeTypeWrapper<>(MoreMachineRecipeType.STAMPING, StamperRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_STAMPER);

    public static final RVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> LATHE = new RVRecipeTypeWrapper<>(MoreMachineRecipeType.LATHING, ItemStackToItemStackRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_LATHE);

    public static final RVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> ROLLING_MILL = new RVRecipeTypeWrapper<>(MoreMachineRecipeType.ROLLING_MILL, ItemStackToItemStackRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_ROLLING_MILL);

    public static final FakeRVRecipeType<MMBasicItemStackChemicalToItemStackRecipe> REPLICATOR = new FakeRVRecipeType<>(MMBlocks.REPLICATOR, MMBasicItemStackChemicalToItemStackRecipe.class, -3, -3, 170, 79);

    public static final FakeRVRecipeType<BasicFluidChemicalToFluidRecipe> FLUID_REPLICATOR = new FakeRVRecipeType<>(MMBlocks.FLUID_REPLICATOR, BasicFluidChemicalToFluidRecipe.class, -3, -3, 170, 79);

}
