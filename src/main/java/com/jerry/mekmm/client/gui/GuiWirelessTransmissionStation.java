package com.jerry.mekmm.client.gui;

import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiWirelessTransmissionStation extends GuiConfigurableTile<TileEntityWirelessTransmissionStation, MekanismTileContainer<TileEntityWirelessTransmissionStation>> {

    public GuiWirelessTransmissionStation(MekanismTileContainer<TileEntityWirelessTransmissionStation> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiChemicalGauge(() -> tile.chemicalTank, () -> tile.getChemicalTanks(null), GaugeType.STANDARD, this, 7, 4));
        addRenderableWidget(new GuiFluidGauge(() -> tile.fluidTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD, this, 28, 4));
        addRenderableWidget(new GuiEnergyGauge(tile.getEnergyContainer(), GaugeType.STANDARD, this, 151, 4));
    }
}
