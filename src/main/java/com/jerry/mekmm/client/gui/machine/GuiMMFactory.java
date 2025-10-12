package com.jerry.mekmm.client.gui.machine;

import com.jerry.mekmm.client.gui.element.tab.MMGuiSortingTab;
import com.jerry.mekmm.client.jei.MoreMachineJEIRecipeType;
import com.jerry.mekmm.common.tile.factory.TileEntityMMFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityPlantingFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityReplicatingFactory;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiMMFactory extends GuiConfigurableTile<TileEntityMMFactory<?>, MekanismTileContainer<TileEntityMMFactory<?>>> {

    public GuiMMFactory(MekanismTileContainer<TileEntityMMFactory<?>> container, Inventory inv, Component title) {
        super(container, inv, title);
        if (tile.hasSecondaryResourceBar()) {
            imageHeight += 11;
            inventoryLabelY = 85;
            if (tile instanceof TileEntityPlantingFactory) {
                imageHeight += 20;
                inventoryLabelY = 105;
            }
        } else {
            inventoryLabelY = 75;
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
        super.addGuiElements();
        addRenderableWidget(new MMGuiSortingTab(this, tile));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16, tile instanceof TileEntityPlantingFactory ? 73 : 52))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0));

        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getLastUsage));
        if (tile.hasSecondaryResourceBar()) {
            if (tile instanceof TileEntityPlantingFactory factory) {
                addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(factory.getGasTank(), tile.getGasTanks(null)), 7, 96,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                addRenderableWidget(new GuiDumpButton<>(this, (TileEntityMMFactory<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 96));
            }
            if (tile instanceof TileEntityReplicatingFactory factory) {
                addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(factory.getGasTank(), tile.getGasTanks(null)), 7, 76,
                        tile.tier == FactoryTier.ULTIMATE ? 172 : 138, 4, true))
                        .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                addRenderableWidget(new GuiDumpButton<>(this, (TileEntityMMFactory<?> & IHasDumpButton) tile, tile.tier == FactoryTier.ULTIMATE ? 182 : 148, 76));
            }
        }

        int baseX = tile.tier == FactoryTier.BASIC ? 55 : tile.tier == FactoryTier.ADVANCED ? 35 : tile.tier == FactoryTier.ELITE ? 29 : 27;
        int baseXMult = tile.tier == FactoryTier.BASIC ? 38 : tile.tier == FactoryTier.ADVANCED ? 26 : 19;
        for (int i = 0; i < tile.tier.processes; i++) {
            int cacheIndex = i;
            addProgress(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + baseX + (i * baseXMult), 33))
                    //Only can happen if recipes change because inputs are sanitized in the factory based on the output
                    .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
        }
    }

    private GuiProgress addProgress(GuiProgress progressBar) {
        MekanismJEIRecipeType<?> jeiType = switch (tile.getMMFactoryType()) {
            case RECYCLING -> MoreMachineJEIRecipeType.RECYCLING;
            case PLANTING -> MoreMachineJEIRecipeType.PLANTING;
            case CNC_STAMPING -> MoreMachineJEIRecipeType.CNC_STAMPING;
            case CNC_LATHING -> MoreMachineJEIRecipeType.CNC_LATHING;
            case CNC_ROLLING_MILL -> MoreMachineJEIRecipeType.CNC_ROLLING_MILL;
            case REPLICATING -> MoreMachineJEIRecipeType.REPLICATOR;
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
