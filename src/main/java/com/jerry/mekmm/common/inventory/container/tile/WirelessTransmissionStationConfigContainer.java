package com.jerry.mekmm.common.inventory.container.tile;

import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class WirelessTransmissionStationConfigContainer extends MekanismTileContainer<TileEntityWirelessTransmissionStation> {

    public WirelessTransmissionStationConfigContainer(int id, Inventory inv, @NotNull TileEntityWirelessTransmissionStation tileEntityWirelessTransmissionStation) {
        super(MoreMachineContainerTypes.WIRELESS_TRANSMISSION_STATION_CONFIG, id, inv, tileEntityWirelessTransmissionStation);
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + 50;
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 95;//相比于矿机增加了7
    }

    @Override
    protected void addSlots() {
        //Don't add the tile's slots
    }

    @Override
    protected void addContainerTrackers() {
        tile.addConfigContainerTrackers(this);
    }
}
