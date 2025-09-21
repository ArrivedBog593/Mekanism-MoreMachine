package com.jerry.mekaf.common.inventory.container.tile;

import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekaf.common.tile.base.TileEntityAdvancedFactoryBase;
import com.jerry.mekaf.common.tile.base.TileEntityGasToGasFactory;
import com.jerry.mekaf.common.tile.base.TileEntitySlurryToSlurryFactory;
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
//        if (tile.hasExtrasResourceBar()) {
//            return tile instanceof TileEntityGasToGasFactory<?> ? 121 : tile instanceof TileEntityPressurizedReactingFactory ? 103 : 108;
//        }
//        return tile instanceof TileEntityChemicalToChemicalAdvancedFactory<?> ? 112 : tile instanceof TileEntityLiquifyingFactory ? 85 : 98;
        if (tile.hasExtrasResourceBar()) {
            if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
                return 121;
            }
            return 108;
        }
        if (tile instanceof TileEntityGasToGasFactory<?> || tile instanceof TileEntitySlurryToSlurryFactory<?>) {
            return 112;
        } else {
            return 98;
        }
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
