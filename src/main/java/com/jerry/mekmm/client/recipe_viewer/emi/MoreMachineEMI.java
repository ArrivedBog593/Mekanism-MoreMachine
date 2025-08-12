package com.jerry.mekmm.client.recipe_viewer.emi;

import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerUtils;
import com.jerry.mekmm.client.recipe_viewer.emi.recipe.*;
import com.jerry.mekmm.common.registries.MMChemicals;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import mekanism.client.recipe_viewer.emi.ChemicalEmiStack;
import mekanism.client.recipe_viewer.emi.recipe.ItemStackToItemStackEmiRecipe;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;

import static mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes;

@EmiEntrypoint
public class MoreMachineEMI implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        addCategories(registry);
    }

    private void addCategories(EmiRegistry registry) {
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.RECYCLER, RecyclerEmiRecipe::new);
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.PLANTING_STATION, PlantingEmiRecipe::new);

        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.REPLICATOR, ReplicatorEmiRecipe::new, MMRecipeViewerUtils.getItemReplicatorRecipes());
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.FLUID_REPLICATOR, FluidReplicatorEmiRecipe::new, MMRecipeViewerUtils.getFluidReplicatorRecipes());
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.CHEMICAL_REPLICATOR, ChemicalReplicatorEmiRecipe::new, MMRecipeViewerUtils.getChemicalReplicatorRecipes());

        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.STAMPING, StamperEmiRecipe::new);
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.LATHE, ItemStackToItemStackEmiRecipe::new);
        addCategoryAndRecipes(registry, MMRecipeViewerRecipeType.ROLLING_MILL, ItemStackToItemStackEmiRecipe::new);

        registry.addRecipe(new EmiInfoRecipe(List.of(new ChemicalEmiStack(MMChemicals.UNSTABLE_DIMENSIONAL_GAS.asStack(FluidType.BUCKET_VOLUME))), List.of(
                MekanismLang.RECIPE_VIEWER_INFO_HEAVY_WATER.translate()), Mekanism.rl("info/heavy_water")));
    }
}
