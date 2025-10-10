package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.tile.machine.*;
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

    // Recycler
    public static final MMMachine.MMFactoryMachine<TileEntityRecycler> RECYCLER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.RECYCLER, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.RECYCLING)
            .withGui(() -> MMContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .withComputerSupport("recycler")
            .build();
    // Planting Station
    public static final MMMachine.MMFactoryMachine<TileEntityPlantingStation> PLANTING_STATION = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.PLANTING_STATION, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.PLANTING)
            .withGui(() -> MMContainerTypes.PLANTING_STATION)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .withComputerSupport("plantingStation")
            .build();
    // CNC Stamper
    public static final MMMachine.MMFactoryMachine<TileEntityStamper> CNC_STAMPER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_STAMPER, MekanismLang.DESCRIPTION_CRUSHER, MMFactoryType.CNC_STAMPING)
            .withGui(() -> MMContainerTypes.CNC_STAMPER)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MekanismConfig.usage.crusher, MekanismConfig.storage.crusher)
            .withComputerSupport("cnc_stamper")
            .build();
    // CNC Lathe
    public static final MMMachine.MMFactoryMachine<TileEntityLathe> CNC_LATHE = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_LATHE, MekanismLang.DESCRIPTION_CRUSHER, MMFactoryType.CNC_LATHING)
            .withGui(() -> MMContainerTypes.CNC_LATHE)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MekanismConfig.usage.crusher, MekanismConfig.storage.crusher)
            .withComputerSupport("cnc_lathe")
            .build();
    // CNC Rolling Mill
    public static final MMMachine.MMFactoryMachine<TileEntityRollingMill> CNC_ROLLING_MILL = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_ROLLING_MILL, MekanismLang.DESCRIPTION_CRUSHER, MMFactoryType.CNC_ROLLING_MILL)
            .withGui(() -> MMContainerTypes.CNC_ROLLING_MILL)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MekanismConfig.usage.crusher, MekanismConfig.storage.crusher)
            .withComputerSupport("cnc_rolling_mill")
            .build();
    // Replicator
    public static final MMMachine.MMFactoryMachine<TileEntityReplicator> REPLICATOR = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.REPLICATOR, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.REPLICATING)
            .withGui(() -> MMContainerTypes.REPLICATOR)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .withComputerSupport("replicator")
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
