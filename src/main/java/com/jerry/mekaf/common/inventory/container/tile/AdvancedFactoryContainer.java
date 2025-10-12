package com.jerry.mekaf.common.inventory.container.tile;

import com.jerry.mekaf.common.registries.AdvancedFactoryContainerTypes;
import com.jerry.mekaf.common.tile.factory.TileEntityAdvancedFactoryBase;
import com.jerry.mekaf.common.tile.factory.TileEntityChemicalToChemicalFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityLiquifyingFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityPressurizedReactingFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedFactoryContainer extends MekanismTileContainer<TileEntityAdvancedFactoryBase<?>> {

    public AdvancedFactoryContainer(int id, Inventory inv, TileEntityAdvancedFactoryBase<?> tile) {
        super(AdvancedFactoryContainerTypes.ADVANCED_FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        if (tile.hasExtraResourceBar()) {
            return tile instanceof TileEntityChemicalToChemicalFactory<?> ? 121 : tile instanceof TileEntityPressurizedReactingFactory ? 103 : 108;
        }
        return tile instanceof TileEntityChemicalToChemicalFactory<?> ? 112 : tile instanceof TileEntityLiquifyingFactory ? 85 : 98;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
