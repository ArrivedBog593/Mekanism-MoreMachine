package com.jerry.mekmm.client.recipe_viewer.emi.recipe;

import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
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

public class ReplicatorEmiRecipe extends MekanismEmiRecipe<MMBasicItemStackChemicalToItemStackRecipe> {

    public ReplicatorEmiRecipe(MekanismEmiRecipeCategory category, ResourceLocation id, MMBasicItemStackChemicalToItemStackRecipe mmBasicItemStackChemicalToItemStackRecipe) {
        super(category, id, mmBasicItemStackChemicalToItemStackRecipe);
        addInputDefinition(recipe.getItemInput());
        addInputDefinition(recipe.getChemicalInput());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        GaugeType chemical = GaugeType.STANDARD.with(DataType.INPUT);
        initTank(widgetHolder, GuiChemicalGauge.getDummy(chemical, this, 7, 4), input(0));
        addSlot(widgetHolder, SlotType.EXTRA, 8, 65).with(SlotOverlay.MINUS);
        addSlot(widgetHolder, SlotType.INPUT, 29, 32, input(0));
        addSlot(widgetHolder, SlotType.OUTPUT, 131, 32);
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
