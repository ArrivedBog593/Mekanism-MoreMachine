package com.jerry.mekmm.client.recipe_viewer.emi.recipe;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.common.tile.machine.TileEntityPlantingStation;
import dev.emi.emi.api.widget.WidgetHolder;
import mekanism.api.functions.ConstantPredicates;
import mekanism.client.gui.element.bar.GuiEmptyBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.inventory.container.slot.SlotOverlay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlantingEmiRecipe extends MekanismEmiHolderRecipe<PlantingRecipe> {

    private static final int PROCESS_TIME = TileEntityPlantingStation.BASE_TICKS_REQUIRED;

    public PlantingEmiRecipe(MekanismEmiRecipeCategory category, RecipeHolder<PlantingRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getItemInput());
        addInputDefinition(recipe.getChemicalInput(), recipe.perTickUsage() ? PROCESS_TIME : 1);
        List<ItemStack> firstOutput = new ArrayList<>();
        List<ItemStack> secondOutput = new ArrayList<>();
        for (PlantingRecipe.PlantingStationRecipeOutput output : recipe.getOutputDefinition()) {
            firstOutput.add(output.first());
            secondOutput.add(output.second());
        }
        addItemOutputDefinition(firstOutput);
        if (secondOutput.stream().allMatch(ConstantPredicates.ITEM_EMPTY)){
            addOutputDefinition(Collections.emptyList());
        }else {
            addItemOutputDefinition(secondOutput);
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        addSlot(widgets, SlotType.INPUT, 56, 17, input(0));
        addSlot(widgets, SlotType.EXTRA, 56, 53, catalyst(0)).catalyst(true);
        GuiSlot output = addSlot(widgets, SlotType.OUTPUT_WIDE, 112, 31);
        initItem(widgets, output.getX() + 4, output.getY() + 4, output(0)).recipeContext(this);
        initItem(widgets, output.getX() + 20, output.getY() + 4, output(1)).recipeContext(this);
        addSlot(widgets, SlotType.POWER, 31, 35).with(SlotOverlay.POWER);
        addElement(widgets, new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 164, 15));
        initTank(widgets, new GuiEmptyBar(this, 60, 36, 6, 12), input(1));
        addSimpleProgress(widgets, ProgressType.BAR, 78, 38, PROCESS_TIME);
    }
}
