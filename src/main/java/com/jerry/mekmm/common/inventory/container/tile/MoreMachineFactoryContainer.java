package com.jerry.mekmm.common.inventory.container.tile;

import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
import com.jerry.mekmm.common.tile.factory.TileEntityMoreMachineFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityPlantingFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;

public class MoreMachineFactoryContainer extends MekanismTileContainer<TileEntityMoreMachineFactory<?>> {

    public MoreMachineFactoryContainer(int id, Inventory inv, TileEntityMoreMachineFactory<?> tile) {
        super(MoreMachineContainerTypes.MM_FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        if (tile.hasSecondaryResourceBar()) {
            return tile instanceof TileEntityPlantingFactory ? 115 : 95;
        }
        return 85;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
