package com.jerry.mekmm.client.jei;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.jei.machine.*;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.client.jei.machine.ItemStackToItemStackRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class MoreMachineJEI implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Mekmm.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new RecyclerRecipeCategory(guiHelper, MoreMachineJEIRecipeType.RECYCLING));
        registration.addRecipeCategories(new PlantingRecipeCategory(guiHelper, MoreMachineJEIRecipeType.PLANTING));

        registration.addRecipeCategories(new StamperRecipeCategory(guiHelper, MoreMachineJEIRecipeType.CNC_STAMPING));
        registration.addRecipeCategories(new ItemStackToItemStackRecipeCategory(guiHelper, MoreMachineJEIRecipeType.CNC_LATHING, MMBlocks.CNC_LATHE));
        registration.addRecipeCategories(new ItemStackToItemStackRecipeCategory(guiHelper, MoreMachineJEIRecipeType.CNC_ROLLING_MILL, MMBlocks.CNC_ROLLING_MILL));

        registration.addRecipeCategories(new ReplicatorRecipeCategory(guiHelper, MoreMachineJEIRecipeType.REPLICATOR));
        registration.addRecipeCategories(new FluidReplicatorRecipeCategory(guiHelper, MoreMachineJEIRecipeType.FLUID_REPLICATOR));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.RECYCLING, MoreMachineRecipeType.RECYCLING);
        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.PLANTING, MoreMachineRecipeType.PLANTING);

        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.CNC_STAMPING, MoreMachineRecipeType.STAMPING);
        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.CNC_LATHING, MoreMachineRecipeType.LATHING);
        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.CNC_ROLLING_MILL, MoreMachineRecipeType.ROLLING_MILL);

        MoreMachineRecipeRegistryHelper.registerItemReplicator(registry);
        MoreMachineRecipeRegistryHelper.registerFluidReplicator(registry);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry, MMBlocks.RECYCLER);
        CatalystRegistryHelper.register(registry, MMBlocks.PLANTING_STATION, MekanismJEIRecipeType.GAS_CONVERSION);

        CatalystRegistryHelper.register(registry, MMBlocks.CNC_STAMPER);
        CatalystRegistryHelper.register(registry, MMBlocks.CNC_LATHE);
        CatalystRegistryHelper.register(registry, MMBlocks.CNC_ROLLING_MILL);

        CatalystRegistryHelper.register(registry, MMBlocks.REPLICATOR);
        CatalystRegistryHelper.register(registry, MMBlocks.FLUID_REPLICATOR);
    }
}
