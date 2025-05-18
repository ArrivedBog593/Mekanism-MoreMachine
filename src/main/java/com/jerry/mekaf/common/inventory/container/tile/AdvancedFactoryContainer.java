package com.jerry.mekaf.common.inventory.container.tile;

import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekaf.common.tile.factory.TileEntityAdvancedFactoryBase;
import com.jerry.mekaf.common.tile.factory.TileEntityChemicalToChemicalAdvancedFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedFactoryContainer extends MekanismTileContainer<TileEntityAdvancedFactoryBase<?>> {

    public AdvancedFactoryContainer(int id, Inventory inv, TileEntityAdvancedFactoryBase<?> tile) {
        super(AFContainerTypes.ADVANCED_FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        if (tile.hasSecondaryResourceBar()) {
            return tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 121 : 108;
        }
        return 98;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
