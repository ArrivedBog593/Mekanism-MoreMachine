package com.jerry.mekaf.common.inventory.container.tile;

import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekaf.common.tile.base.TileEntityAdvancedFactoryBase;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AdvancedFactoryContainer extends MekanismTileContainer<TileEntityAdvancedFactoryBase<?>> {

    public AdvancedFactoryContainer(int id, Inventory inv, @NotNull TileEntityAdvancedFactoryBase<?> tile) {
        super(AFContainerTypes.ADVANCED_FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
//        if (tile.hasSecondaryResourceBar()) {
//            return tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 121 : tile instanceof TileEntityPressurizedReactingFactory ? 103 : 108;
//        }
//        return tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 112 : tile instanceof TileEntityLiquifyingFactory ? 85 : 98;
        if (tile.hasSecondaryResourceBar()) {
            return 108;
        }
        return 98;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
