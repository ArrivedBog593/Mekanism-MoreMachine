package com.jerry.mekmm.client.recipe_viewer.emi.recipe;

import com.jerry.mekmm.api.recipes.basic.BasicFluidChemicalToFluidRecipe;
import com.jerry.mekmm.common.tile.machine.TileEntityReplicator;
import dev.emi.emi.api.widget.WidgetHolder;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiRecipe;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.resources.ResourceLocation;

public class FluidReplicatorEmiRecipe extends MekanismEmiRecipe<BasicFluidChemicalToFluidRecipe> {

    public FluidReplicatorEmiRecipe(MekanismEmiRecipeCategory category, ResourceLocation id, BasicFluidChemicalToFluidRecipe mmBasicItemStackChemicalToItemStackRecipe) {
        super(category, id, mmBasicItemStackChemicalToItemStackRecipe);
        addInputDefinition(recipe.getFluidInput());
        addInputDefinition(recipe.getChemicalInput());
        addFluidOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        GaugeType chemical = GaugeType.STANDARD.with(DataType.INPUT);
        initTank(widgetHolder, GuiChemicalGauge.getDummy(chemical, this, 7, 4), input(0));
        GaugeType fluid_input = GaugeType.STANDARD.with(DataType.INPUT);
        initTank(widgetHolder, GuiChemicalGauge.getDummy(fluid_input, this, 28, 4), input(0));
        GaugeType fluid_output = GaugeType.STANDARD.with(DataType.OUTPUT);
        initTank(widgetHolder, GuiChemicalGauge.getDummy(fluid_output, this, 131, 4), input(1));
        addSlot(widgetHolder, SlotType.EXTRA, 8, 65).with(SlotOverlay.MINUS);
        addSlot(widgetHolder, SlotType.INPUT, 29, 65).with(SlotOverlay.PLUS);
        addSlot(widgetHolder, SlotType.OUTPUT, 131, 65).with(SlotOverlay.PLUS);
        addSlot(widgetHolder, SlotType.POWER, 152, 65).with(SlotOverlay.POWER);
        addSimpleProgress(widgetHolder, ProgressType.LARGE_RIGHT, 64, 36, TileEntityReplicator.BASE_TICKS_REQUIRED);
        addElement(widgetHolder, new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            @Override
            public long getEnergy() {
                return 1L;
            }

            @Override
            public long getMaxEnergy() {
                return 1L;
            }
        }, GaugeType.STANDARD, this, 151, 4));
    }
}
