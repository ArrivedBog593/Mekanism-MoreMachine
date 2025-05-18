package com.jerry.mekaf.client.gui.machine;

import com.jerry.mekaf.client.gui.element.tab.AFGuiSortingTab;
import com.jerry.mekaf.common.tile.factory.TileEntityAdvancedFactoryBase;
import com.jerry.mekaf.common.tile.factory.TileEntityChemicalToChemicalAdvancedFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityItemToChemicalAdvancedFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityWashingFactory;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.SpecialColors;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.GuiSideHolder;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiFluidBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiAdvancedFactory extends GuiConfigurableTile<TileEntityAdvancedFactoryBase<?>, MekanismTileContainer<TileEntityAdvancedFactoryBase<?>>> {

    @Nullable
    private GuiDumpButton<?> dumpButton;

    public GuiAdvancedFactory(MekanismTileContainer<TileEntityAdvancedFactoryBase<?>> container, Inventory inv, Component title) {
        super(container, inv, title);
        imageHeight += 13;
        if (tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?>) imageHeight += 13;
        if (tile.hasSecondaryResourceBar()) {
            imageHeight += 11;
            inventoryLabelY = tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 111 : 98;
        } else {
            inventoryLabelY = 88;
        }

        if (tile.tier == FactoryTier.ULTIMATE) {
            imageWidth += 34;
            inventoryLabelX = 26;
        }
        titleLabelY = 4;
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        if (tile instanceof TileEntityWashingFactory) {
            addRenderableWidget(GuiSideHolder.create(this, imageWidth, 66, 57, false, true, SpecialColors.TAB_CHEMICAL_WASHER));
        }
        super.addGuiElements();
        addRenderableWidget(new AFGuiSortingTab(this, tile));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16, 52))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getLastUsage));

        if (tile.hasSecondaryResourceBar()) {
            if (tile instanceof TileEntityWashingFactory) {
                addRenderableWidget(new GuiFluidBar(this, GuiFluidBar.getProvider(((TileEntityWashingFactory) tile).getFluidTankBar(), tile.getFluidTanks(null)), 7, 102,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                dumpButton = addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 102));
            } else {
                addRenderableWidget(new GuiChemicalBar(this, GuiChemicalBar.getProvider(tile.getChemicalTankBar(), tile.getChemicalTanks(null)),
                        7, tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 102 : 89,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                dumpButton = addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 102 : 89));
            }
        }

        if (tile instanceof TileEntityItemToChemicalAdvancedFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.chemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 57))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        if (tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.inputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT, index));
                addRenderableWidget(new GuiChemicalGauge(() -> factory.outputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 70))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        for (int i = 0; i < tile.tier.processes; i++) {
            int cacheIndex = i;
            addRenderableWidget(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + tile.getXPos(i), tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 46 : 33))
                    .recipeViewerCategory(tile)
                    //Only can happen if recipes change because inputs are sanitized in the factory based on the output
                    .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics, dumpButton == null ? getXSize() : dumpButton.getRelativeX());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
