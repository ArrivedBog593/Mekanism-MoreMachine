package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.tile.machine.TileEntityRecycler;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

public class MMBlockTypes {

    private MMBlockTypes() {

    }

    private static final Table<FactoryTier, MMFactoryType, MMFactory<?>> FACTORIES = HashBasedTable.create();

    // Precision Sawmill
    public static final MMMachine.MMFactoryMachine<TileEntityRecycler> RECYCLER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.RECYCLER, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.RECYCLING)
            .withGui(() -> MMContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .withComputerSupport("recycler")
            .build();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MMFactoryType type : MMEnumUtils.MM_FACTORY_TYPES) {
                FACTORIES.put(tier, type, MMFactory.MMFactoryBuilder.createMMFactory(() -> MMTileEntityTypes.getMMFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    public static MMFactory<?> getMMFactory(FactoryTier tier, MMFactoryType type) {
        return FACTORIES.get(tier, type);
    }
}
