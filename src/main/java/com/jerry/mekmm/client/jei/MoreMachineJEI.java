package com.jerry.mekmm.client.jei;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.jei.machine.PlantingRecipeCategory;
import com.jerry.mekmm.client.jei.machine.RecyclerRecipeCategory;
import com.jerry.mekmm.client.jei.machine.ReplicatorRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerUtils;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.JeiGuiElementHandler;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.jei.RecipeRegistryHelper;
import mekanism.client.recipe_viewer.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
@NothingNullByDefault
public class MoreMachineJEI implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        // 不能使用Mekmm.rl()，原因见MekanismJEI.class
        return ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new RecyclerRecipeCategory(guiHelper, MMRecipeViewerRecipeType.RECYCLER));
        registry.addRecipeCategories(new PlantingRecipeCategory(guiHelper, MMRecipeViewerRecipeType.PLANTING_STATION));

        registry.addRecipeCategories(new ReplicatorRecipeCategory(guiHelper, MMRecipeViewerRecipeType.REPLICATOR));

        registry.addRecipeCategories(new ItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.STAMPING));
        registry.addRecipeCategories(new ItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.LATHE));
        registry.addRecipeCategories(new ItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.ROLLING_MILL));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        registry.addGenericGuiContainerHandler(GuiMekanism.class, new JeiGuiElementHandler(registry.getJeiHelpers().getIngredientManager()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.RECYCLER, MMRecipeType.RECYCLER);
        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.PLANTING_STATION, MMRecipeType.PLANTING_STATION);

        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.REPLICATOR, MMRecipeViewerUtils.getReplicatorRecipes());

        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.STAMPING, MMRecipeType.STAMPING);
        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.LATHE, MMRecipeType.LATHE);
        RecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.ROLLING_MILL, MMRecipeType.ROLLING_MILL);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        CatalystRegistryHelper.register(registry, MMRecipeViewerRecipeType.RECYCLER, MMRecipeViewerRecipeType.PLANTING_STATION, MMRecipeViewerRecipeType.REPLICATOR,
                MMRecipeViewerRecipeType.STAMPING, MMRecipeViewerRecipeType.LATHE, MMRecipeViewerRecipeType.ROLLING_MILL);

        CatalystRegistryHelper.register(registry, RecipeViewerRecipeType.CHEMICAL_CONVERSION);
    }
}
