package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.content.blocktype.MMBlockShapes;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.tile.TileEntityDoll;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MMBlockTypes {

    private MMBlockTypes() {
    }

    private static final Table<FactoryTier, MMFactoryType, MMFactory<?>> MM_FACTORIES = HashBasedTable.create();

    // Recycler
    public static final MMMachine.MMFactoryMachine<TileEntityRecycler> RECYCLER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.RECYCLER, MMLang.DESCRIPTION_RECYCLER, MMFactoryType.RECYCLING)
            .withGui(() -> MMContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MMConfig.usage.recycler, MMConfig.storage.recycler)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("recycler")
            .build();

    // Planting Station
    public static final MMMachine.MMFactoryMachine<TileEntityPlantingStation> PLANTING_STATION = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.PLANTING_STATION, MMLang.DESCRIPTION_PLANTING_STATION, MMFactoryType.PLANTING_STATION)
            .withGui(() -> MMContainerTypes.PLANTING_STATION)
            .withSound(MekanismSounds.ENRICHMENT_CHAMBER)
            .withEnergyConfig(MMConfig.usage.plantingStation, MMConfig.storage.plantingStation)
            .with(AttributeUpgradeSupport.DEFAULT_ADVANCED_MACHINE_UPGRADES)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withComputerSupport("plantingStation")
            .build();

    // CNC Stamper
    public static final MMMachine.MMFactoryMachine<TileEntityStamper> CNC_STAMPER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_STAMPER, MMLang.DESCRIPTION_CNC_STAMPER, MMFactoryType.CNC_STAMPING)
            .withGui(() -> MMContainerTypes.CNC_STAMPER)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MMConfig.usage.cnc_stamper, MMConfig.storage.cnc_stamper)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_stamper")
            .build();

    // CNC Lathe
    public static final MMMachine.MMFactoryMachine<TileEntityLathe> CNC_LATHE = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_LATHE, MMLang.DESCRIPTION_CNC_LATHE, MMFactoryType.CNC_LATHING)
            .withGui(() -> MMContainerTypes.CNC_LATHE)
            .withSound(MekanismSounds.OSMIUM_COMPRESSOR)
            .withEnergyConfig(MMConfig.usage.cnc_lathe, MMConfig.storage.cnc_lathe)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_lathe")
            .build();

    // CNC Rolling Mill
    public static final MMMachine.MMFactoryMachine<TileEntityRollingMill> CNC_ROLLING_MILL = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_ROLLING_MILL, MMLang.DESCRIPTION_CNC_ROLLING_MILL, MMFactoryType.CNC_ROLLING_MILL)
            .withGui(() -> MMContainerTypes.CNC_ROLLING_MILL)
            .withSound(MekanismSounds.COMBINER)
            .withEnergyConfig(MMConfig.usage.cnc_rollingMill, MMConfig.storage.cnc_rollingMill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_rolling_mill")
            .build();

    // Replicator
    public static final MMMachine.MMFactoryMachine<TileEntityReplicator> REPLICATOR = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.REPLICATOR, MMLang.DESCRIPTION_REPLICATOR, MMFactoryType.REPLICATING)
            .withGui(() -> MMContainerTypes.REPLICATOR)
            .withEnergyConfig(MMConfig.usage.replicator, MMConfig.storage.replicator)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withComputerSupport("replicator")
            .build();

    public static final MMMachine.MMFactoryMachine<TileEntityFluidReplicator> FLUID_REPLICATOR = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.FLUID_REPLICATOR, MMLang.DESCRIPTION_REPLICATOR, MMFactoryType.REPLICATING)
            .withGui(() -> MMContainerTypes.FLUID_REPLICATOR)
            .withEnergyConfig(MMConfig.usage.replicator, MMConfig.storage.replicator)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .withSideConfig(TransmissionType.FLUID, TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withComputerSupport("fluidReplicator")
            .build();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MMFactoryType type : MMEnumUtils.MM_FACTORY_TYPES) {
                MM_FACTORIES.put(tier, type, MMFactory.MMFactoryBuilder.createMMFactory(() -> MMTileEntityTypes.getMMFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    // Ambient Gas Collector
    public static final Machine<TileEntityAmbientGasCollector> AMBIENT_GAS_COLLECTOR = Machine.MachineBuilder
            .createMachine(() -> MMTileEntityTypes.AMBIENT_GAS_COLLECTOR, MMLang.DESCRIPTION_AMBIENT_GAS_COLLECTOR)
            .withGui(() -> MMContainerTypes.AMBIENT_GAS_COLLECTOR)
            .withEnergyConfig(MMConfig.usage.ambientGasCollector, MMConfig.storage.ambientGasCollector)
            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.ENERGY)
            .withCustomShape(BlockShapes.ELECTRIC_PUMP)
            .withComputerSupport("ambientGasCollector")
            .replace(Attributes.ACTIVE)
            .build();

    // Author Doll
    public static final BlockTypeTile<TileEntityDoll> AUTHOR_DOLL = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> MMTileEntityTypes.AUTHOR_DOLL, MMLang.AUTHOR_DOLL)
            .with(new AttributeStateFacing(BlockStateProperties.HORIZONTAL_FACING))
            .withCustomShape(MMBlockShapes.AUTHOR_DOLL)
            .with(AttributeCustomSelectionBox.JSON)
            .build();

    public static MMFactory<?> getMMFactory(FactoryTier tier, MMFactoryType type) {
        return MM_FACTORIES.get(tier, type);
    }
}
