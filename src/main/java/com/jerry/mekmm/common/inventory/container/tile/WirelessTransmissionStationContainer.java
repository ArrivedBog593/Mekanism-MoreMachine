package com.jerry.mekmm.common.inventory.container.tile;

import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class WirelessTransmissionStationContainer extends MekanismTileContainer<TileEntityWirelessTransmissionStation> {

    public WirelessTransmissionStationContainer(int id, Inventory inv, @NotNull TileEntityWirelessTransmissionStation tileEntityWirelessTransmissionStation) {
        super(MoreMachineContainerTypes.WIRELESS_TRANSMISSION_STATION, id, inv, tileEntityWirelessTransmissionStation);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 14;
    }
}
