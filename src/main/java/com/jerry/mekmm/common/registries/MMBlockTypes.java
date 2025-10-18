package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine.MMFactoryMachine;
import com.jerry.mekmm.common.content.blocktype.MMMachine.MMMachineBuilder;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

import java.util.EnumSet;

public class MMBlockTypes {

    private MMBlockTypes() {

    }

    private static final Table<FactoryTier, MMFactoryType, MMFactory<?>> FACTORIES = HashBasedTable.create();

    // Recycler
    public static final MMFactoryMachine<TileEntityRecycler> RECYCLER = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.RECYCLER, MMLang.DESCRIPTION_RECYCLER, MMFactoryType.RECYCLING)
            .withGui(() -> MMContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MoreMachineConfig.usage.recycler, MoreMachineConfig.storage.recycler)
            .withComputerSupport("recycler")
            .build();
    // Planting Station
    public static final MMFactoryMachine<TileEntityPlantingStation> PLANTING_STATION = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.PLANTING_STATION, MMLang.DESCRIPTION_PLANTING_STATION, MMFactoryType.PLANTING)
            .withGui(() -> MMContainerTypes.PLANTING_STATION)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MoreMachineConfig.usage.plantingStation, MoreMachineConfig.storage.plantingStation)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, Upgrade.GAS))
            .withComputerSupport("plantingStation")
            .build();
    // CNC Stamper
    public static final MMFactoryMachine<TileEntityStamper> CNC_STAMPER = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_STAMPER, MMLang.DESCRIPTION_CNC_STAMPER, MMFactoryType.CNC_STAMPING)
            .withGui(() -> MMContainerTypes.CNC_STAMPER)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_stamper, MoreMachineConfig.storage.cnc_stamper)
            .withComputerSupport("cnc_stamper")
            .build();
    // CNC Lathe
    public static final MMFactoryMachine<TileEntityLathe> CNC_LATHE = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_LATHE, MMLang.DESCRIPTION_CNC_LATHE, MMFactoryType.CNC_LATHING)
            .withGui(() -> MMContainerTypes.CNC_LATHE)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_lathe, MoreMachineConfig.storage.cnc_lathe)
            .withComputerSupport("cnc_lathe")
            .build();
    // CNC Rolling Mill
    public static final MMFactoryMachine<TileEntityRollingMill> CNC_ROLLING_MILL = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_ROLLING_MILL, MMLang.DESCRIPTION_CNC_ROLLING_MILL, MMFactoryType.CNC_ROLLING_MILL)
            .withGui(() -> MMContainerTypes.CNC_ROLLING_MILL)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_rollingMill, MoreMachineConfig.storage.cnc_rollingMill)
            .withComputerSupport("cnc_rolling_mill")
            .build();
    // Replicator
    public static final MMFactoryMachine<TileEntityReplicator> REPLICATOR = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.REPLICATOR, MMLang.DESCRIPTION_REPLICATOR, MMFactoryType.REPLICATING)
            .withGui(() -> MMContainerTypes.REPLICATOR)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MoreMachineConfig.usage.itemReplicator, MoreMachineConfig.storage.itemReplicator)
            .withComputerSupport("replicator")
            .build();
    // Fluid Replicator
    public static final MMFactoryMachine<TileEntityFluidReplicator> FLUID_REPLICATOR = MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.FLUID_REPLICATOR, MMLang.DESCRIPTION_FLUID_REPLICATOR, MMFactoryType.REPLICATING)
            .withGui(() -> MMContainerTypes.FLUID_REPLICATOR)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MoreMachineConfig.usage.fluidReplicator, MoreMachineConfig.storage.fluidReplicator)
            .withComputerSupport("fluidReplicator")
            .build();
    // Ambient Gas Collector
    public static final Machine<TileEntityAmbientGasCollector> AMBIENT_GAS_COLLECTOR = MachineBuilder
            .createMachine(() -> MMTileEntityTypes.AMBIENT_GAS_COLLECTOR, MekanismLang.DESCRIPTION_PRECISION_SAWMILL)
            .withGui(() -> MMContainerTypes.AMBIENT_GAS_COLLECTOR)
            .withEnergyConfig(MoreMachineConfig.usage.ambientGasCollector, MoreMachineConfig.storage.ambientGasCollector)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY))
            .withCustomShape(BlockShapes.ELECTRIC_PUMP)
            .withComputerSupport("ambientGasCollector")
            .replace(Attributes.ACTIVE)
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
