package com.jerry.mekaf.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekaf.common.tile.TileEntityCentrifugingFactory;
import com.jerry.mekaf.common.tile.TileEntityDissolvingFactory;
import com.jerry.mekaf.common.tile.TileEntityOxidizingFactory;
import com.jerry.mekaf.common.tile.TileEntityWashingFactory;
import com.jerry.mekaf.common.tile.base.TileEntityAdvancedFactoryBase;
import com.jerry.mekmm.Mekmm;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.EnumUtils;

public class AFTileEntityTypes {

    private AFTileEntityTypes() {

    }

    public static final TileEntityTypeDeferredRegister AF_TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, AdvancedFactoryType, TileEntityTypeRegistryObject<? extends TileEntityAdvancedFactoryBase<?>>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            FACTORIES.put(tier, AdvancedFactoryType.OXIDIZING, AF_TILE_ENTITY_TYPES.register(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.OXIDIZING), (pos, state) -> new TileEntityOxidizingFactory(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.OXIDIZING), pos, state), TileEntityMekanism::tickServer, TileEntityMekanism::tickClient));
            FACTORIES.put(tier, AdvancedFactoryType.DISSOLVING, AF_TILE_ENTITY_TYPES.register(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.DISSOLVING), (pos, state) -> new TileEntityDissolvingFactory(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.DISSOLVING), pos, state), TileEntityMekanism::tickServer, TileEntityMekanism::tickClient));
            FACTORIES.put(tier, AdvancedFactoryType.WASHING, AF_TILE_ENTITY_TYPES.register(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.WASHING), (pos, state) -> new TileEntityWashingFactory(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.WASHING), pos, state), TileEntityMekanism::tickServer, TileEntityMekanism::tickClient));
            FACTORIES.put(tier, AdvancedFactoryType.CENTRIFUGING, AF_TILE_ENTITY_TYPES.register(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.CENTRIFUGING), (pos, state) -> new TileEntityCentrifugingFactory(AFBlocks.getAdvancedFactory(tier, AdvancedFactoryType.CENTRIFUGING), pos, state), TileEntityMekanism::tickServer, TileEntityMekanism::tickClient));
        }
    }

    public static TileEntityTypeRegistryObject<? extends TileEntityAdvancedFactoryBase<?>> getAdvancedFactoryTile(FactoryTier tier, AdvancedFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static TileEntityTypeRegistryObject<? extends TileEntityAdvancedFactoryBase<?>>[] getAdvancedFactoryTiles() {
        return FACTORIES.values().toArray(new TileEntityTypeRegistryObject[0]);
    }
}
