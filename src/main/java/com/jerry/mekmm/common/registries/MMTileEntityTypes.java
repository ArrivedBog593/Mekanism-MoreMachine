package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.tile.factory.TileEntityMMFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityRecyclingMMFactory;
import com.jerry.mekmm.common.tile.machine.TileEntityRecycler;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.EnumUtils;

public class MMTileEntityTypes {

    private MMTileEntityTypes() {

    }

    public static final TileEntityTypeDeferredRegister MM_TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, MMFactoryType, TileEntityTypeRegistryObject<? extends TileEntityMMFactory<?>>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            FACTORIES.put(tier, MMFactoryType.RECYCLING, MM_TILE_ENTITY_TYPES.register(MMBlocks.getMMFactory(tier, MMFactoryType.RECYCLING), (pos, state) -> new TileEntityRecyclingMMFactory(MMBlocks.getMMFactory(tier, MMFactoryType.RECYCLING), pos, state), TileEntityMekanism::tickServer, TileEntityMekanism::tickClient));
        }
    }

    public static final TileEntityTypeRegistryObject<TileEntityRecycler> RECYCLER = MM_TILE_ENTITY_TYPES.register(MMBlocks.RECYCLER, TileEntityRecycler::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    public static TileEntityTypeRegistryObject<? extends TileEntityMMFactory<?>> getMMFactoryTile(FactoryTier tier, MMFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static TileEntityTypeRegistryObject<? extends TileEntityMMFactory<?>>[] getFactoryTiles() {
        return FACTORIES.values().toArray(new TileEntityTypeRegistryObject[0]);
    }
}
