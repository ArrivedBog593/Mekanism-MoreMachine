package com.jerry.mekaf.client.gui.machine;

import com.jerry.mekaf.client.gui.element.tab.AFGuiSortingTab;
import com.jerry.mekaf.common.tile.TileEntityWashingFactory;
import com.jerry.mekaf.common.tile.base.*;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.SpecialColors;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.GuiSideHolder;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiFluidBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiMergedChemicalTankGauge;
import mekanism.client.gui.element.gauge.GuiSlurryGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAdvancedFactory extends GuiConfigurableTile<TileEntityAdvancedFactoryBase<?>, MekanismTileContainer<TileEntityAdvancedFactoryBase<?>>> {

    public GuiAdvancedFactory(MekanismTileContainer<TileEntityAdvancedFactoryBase<?>> container, Inventory inv, Component title) {
        super(container, inv, title);
//        imageHeight += tile instanceof TileEntityPressurizedReactingFactory ? 8 : tile instanceof TileEntityLiquifyingFactory ? 0 : 13;
        imageHeight += 13;
        if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
            imageHeight += 13;
        }
        if (tile.hasExtrasResourceBar()) {
            imageHeight += 11;
            if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
                inventoryLabelY = 111;
            } else {
                inventoryLabelY = 98;
            }
//            inventoryLabelY = tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 111 : tile instanceof TileEntityPressurizedReactingFactory ? 93 : 98;
        } else {
            if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
                inventoryLabelY = 103;
            } else {
                inventoryLabelY = 88;
            }
//            inventoryLabelY = tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 103 : tile instanceof TileEntityLiquifyingFactory ? 75 : 88;
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

        if (tile.hasExtrasResourceBar()) {
            if (tile instanceof TileEntityWashingFactory factory) {
                addRenderableWidget(new GuiFluidBar(this, GuiFluidBar.getProvider(factory.getFluidTankBar(), tile.getFluidTanks(null)), 7, 102,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                addRenderableWidget(new GuiDumpButton<>(this, factory, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 102));
            } else {
                addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.getGasTankBar(), tile.getGasTanks(null)),
                        7, 89,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                addRenderableWidget(new GuiDumpButton<>(this, (TileEntityAdvancedFactoryBase<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 89));
            }
        }

        // 物品到气体的工厂只需要一排储罐，物品槽位在TileEntity中被添加
        if (tile instanceof TileEntityItemToGasFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiGasGauge(() -> factory.outputGasTanks.get(index), () -> factory.getGasTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 57))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 物品到混合化学品的工厂只需要一排储罐，物品槽位在TileEntity中被添加
        if (tile instanceof TileEntityItemToMergedFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiMergedChemicalTankGauge<>(() -> factory.outputChemicalTanks.get(index), () -> factory, GaugeType.SMALL, this, factory.getXPos(index) - 1, 57))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 气体生产气体的工厂需要两排储罐
        if (tile instanceof TileEntityGasToGasFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiGasGauge(() -> factory.inputGasTanks.get(index), () -> tile.getGasTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT, index));
                addRenderableWidget(new GuiGasGauge(() -> factory.outputGasTanks.get(index), () -> tile.getGasTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 70))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 浆液生产浆液的工厂需要两排储罐
        if (tile instanceof TileEntitySlurryToSlurryFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiSlurryGauge(() -> factory.inputSlurryTanks.get(index), () -> tile.getSlurryTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT, index));
                addRenderableWidget(new GuiSlurryGauge(() -> factory.outputSlurryTanks.get(index), () -> tile.getSlurryTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 70))
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index));
            }
        }

        // 进度条
        for (int i = 0; i < tile.tier.processes; i++) {
            int cacheIndex = i;
            addProgress(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + tile.getXPos(i), getProgressYPos()))
                    //Only can happen if recipes change because inputs are sanitized in the factory based on the output
                    .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
        }
    }

    private int getProgressYPos() {
        if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
            return 46;
        } else {
            return 33;
        }
    }

    private GuiProgress addProgress(GuiProgress progressBar) {
        MekanismJEIRecipeType<?> jeiType = switch (tile.getAdvancedFactoryType()) {
            case OXIDIZING -> MekanismJEIRecipeType.OXIDIZING;
            case CHEMICAL_INFUSING -> MekanismJEIRecipeType.CHEMICAL_INFUSING;
            case DISSOLVING -> MekanismJEIRecipeType.DISSOLUTION;
            case WASHING -> MekanismJEIRecipeType.WASHING;
            case CENTRIFUGING -> MekanismJEIRecipeType.CENTRIFUGING;
        };
        return addRenderableWidget(progressBar.jeiCategories(jeiType));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
