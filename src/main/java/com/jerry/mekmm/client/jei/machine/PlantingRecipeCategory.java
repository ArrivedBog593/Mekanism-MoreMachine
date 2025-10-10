package com.jerry.mekmm.client.jei.machine;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.bar.GuiEmptyBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class PlantingRecipeCategory extends BaseRecipeCategory<PlantingRecipe> {

    private final GuiBar<?> chemicalInput;
    private final GuiSlot input;
    private final GuiSlot extra;
    private final GuiSlot output;

    public PlantingRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<PlantingRecipe> recipeType) {
        super(helper, recipeType, MMBlocks.PLANTING_STATION, 28, 16, 144, 54);
        input = addSlot(SlotType.INPUT, 56, 17);
        extra = addSlot(SlotType.EXTRA, 56, 53);
        addSlot(SlotType.POWER, 31, 35).with(SlotOverlay.POWER);
        output = addSlot(SlotType.OUTPUT_WIDE, 112, 31);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        chemicalInput = addElement(new GuiEmptyBar(this, 60, 36, 6, 12));
        addSimpleProgress(ProgressType.BAR, 78, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlantingRecipe recipe, IFocusGroup focuses) {
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getItemInput().getRepresentations());
        List<GasStack> scaledChemicals = recipe.getGasInput().getRepresentations();
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, chemicalInput, scaledChemicals);

        List<ItemStack> firstOutputs = new ArrayList<>();
        List<ItemStack> secondOutputs = new ArrayList<>();
        for (PlantingRecipe.PlantingStationRecipeOutput output : recipe.getOutputDefinition()) {
            firstOutputs.add(output.first());
            secondOutputs.add(output.second());
        }

        initItem(builder, RecipeIngredientRole.OUTPUT, output.getX() + 32, output.getY() + 20, firstOutputs);
        if (!secondOutputs.stream().allMatch(ItemStack::isEmpty)) {
            initItem(builder, RecipeIngredientRole.OUTPUT, output.getX() + 48, output.getY() + 20, secondOutputs);
        }
        // 额外
        List<ItemStack> gasItemProviders = new ArrayList<>();
        for (GasStack gasStack : scaledChemicals) {
            gasItemProviders.addAll(MekanismJEI.GAS_STACK_HELPER.getStacksFor(gasStack.getType(), true));
        }
        initItem(builder, RecipeIngredientRole.CATALYST, extra, gasItemProviders);
    }
}
