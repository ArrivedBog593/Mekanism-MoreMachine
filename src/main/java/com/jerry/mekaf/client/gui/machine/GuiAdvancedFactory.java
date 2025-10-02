package com.jerry.mekaf.client.gui.machine;

import com.jerry.mekaf.client.gui.element.tab.AFGuiSortingTab;
import com.jerry.mekaf.common.tile.factory.*;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.SpecialColors;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.GuiSideHolder;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiFluidBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
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
        imageHeight += tile instanceof TileEntityPressurizedReactingFactory ? 8 : tile instanceof TileEntityLiquifyingFactory ? 0 : 13;
        if (tile instanceof TileEntityChemicalToChemicalFactory<?>) imageHeight += 13;
        if (tile.hasExtraResourceBar()) {
            imageHeight += 11;
            inventoryLabelY = tile instanceof TileEntityChemicalToChemicalFactory<?> ? 111 : tile instanceof TileEntityPressurizedReactingFactory ? 93 : 98;
        } else {
            inventoryLabelY = tile instanceof TileEntityChemicalToChemicalFactory<?> ? 103 : tile instanceof TileEntityLiquifyingFactory ? 75 : 88;
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
        if (tile instanceof TileEntityWashingFactory) {
            addRenderableWidget(new GuiDownArrow(this, imageWidth + 8, 90));
        }
        addRenderableWidget(new AFGuiSortingTab(this, tile));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16,
                tile instanceof TileEntityItemToChemicalFactory<?> || tile instanceof TileEntityChemicalToItemFactory<?> ? 65 : tile instanceof TileEntityChemicalToChemicalFactory<?> ? 78 : 52))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getLastUsage));

        if (tile.hasExtraResourceBar()) {
            if (tile instanceof TileEntityWashingFactory factory) {
                addRenderableWidget(new GuiFluidBar(this, GuiFluidBar.getProvider(factory.getFluidTankBar(), tile.getFluidTanks(null)), 7, 102,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                dumpButton = addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 102));
            } else if (tile instanceof TileEntityPressurizedReactingFactory factory) {
                // 出输出化学储罐
                addRenderableWidget(new GuiChemicalGauge(() -> factory.outputChemicalTank, () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, 6, 44))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(TileEntityPressurizedReactingFactory.NOT_ENOUGH_SPACE_GAS_OUTPUT_ERROR, 0));
                // 化学储罐条
                addRenderableWidget(new GuiChemicalBar(this, GuiChemicalBar.getProvider(factory.getChemicalTankBar(), tile.getChemicalTanks(null)), 7, 76,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                // 流体储罐条
                addRenderableWidget(new GuiFluidBar(this, GuiFluidBar.getProvider(factory.getFluidTankBar(), tile.getFluidTanks(null)), 7, 84,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                // dump按钮
                dumpButton = addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 76));
            } else {
                addRenderableWidget(new GuiChemicalBar(this, GuiChemicalBar.getProvider(tile.getChemicalTankBar(), tile.getChemicalTanks(null)),
                        7, tile instanceof TileEntityChemicalToChemicalFactory<?> ? 102 : 89,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                dumpButton = addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148,
                        tile instanceof TileEntityChemicalToChemicalFactory<?> ? 102 : 89));
            }
        }

        if (tile instanceof TileEntityLiquifyingFactory factory) {
            addRenderableWidget(new GuiFluidGauge(() -> factory.fluidTank, () -> factory.getFluidTanks(null), GaugeType.SMALL, this, 6, 44))
                    .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, 0));
        }

        // 物品到气体的工厂只需要一排储罐，物品槽位在TileEntity中被添加
        if (tile instanceof TileEntityItemToChemicalFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.outputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 57))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 气体到物品的工厂只需要一排储罐，但储罐在上面
        if (tile instanceof TileEntityChemicalToItemFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.inputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT, index));
            }
        }

        // 气体生产气体的工厂需要两排储罐
        if (tile instanceof TileEntityChemicalToChemicalFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.inputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT, index));
                addRenderableWidget(new GuiChemicalGauge(() -> factory.outputChemicalTanks.get(index), () -> tile.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 70))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 所有工厂都有的进度条
        for (int i = 0; i < tile.tier.processes; i++) {
            int cacheIndex = i;
            addRenderableWidget(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + tile.getXPos(i),
                    tile instanceof TileEntityChemicalToChemicalFactory<?> || tile instanceof TileEntityChemicalToItemFactory<?> ? 46 : 33))
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
