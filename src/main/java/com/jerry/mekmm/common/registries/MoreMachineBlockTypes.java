package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.common.MoreMachineLang;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.content.blocktype.MoreMachineBlockShapes;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactory;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactory.MoreMachineFactoryBuilder;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import com.jerry.mekmm.common.content.blocktype.MoreMachineMachine.MMMachineBuilder;
import com.jerry.mekmm.common.content.blocktype.MoreMachineMachine.MoreMachineFactoryMachine;
import com.jerry.mekmm.common.tile.TileEntityDoll;
import com.jerry.mekmm.common.tile.TileEntityWirelessChargingStation;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.util.MoreMachineEnumUtils;
import com.jerry.mekmm.common.util.MoreMachineUtils;

import mekanism.api.Upgrade;
import mekanism.common.block.attribute.*;
import mekanism.common.block.attribute.AttributeHasBounding.HandleBoundingBlock;
import mekanism.common.block.attribute.AttributeHasBounding.TriBooleanFunction;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class MoreMachineBlockTypes {

    private MoreMachineBlockTypes() {}

    private static final Table<FactoryTier, MoreMachineFactoryType, MoreMachineFactory<?>> MM_FACTORIES = HashBasedTable.create();

    // Recycler
    public static final MoreMachineFactoryMachine<TileEntityRecycler> RECYCLER = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.RECYCLER, MoreMachineLang.DESCRIPTION_RECYCLER, MoreMachineFactoryType.RECYCLING)
            .withGui(() -> MoreMachineContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MoreMachineConfig.usage.recycler, MoreMachineConfig.storage.recycler)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("recycler")
            .build();

    // Planting Station
    public static final MoreMachineFactoryMachine<TileEntityPlantingStation> PLANTING_STATION = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.PLANTING_STATION, MoreMachineLang.DESCRIPTION_PLANTING_STATION, MoreMachineFactoryType.PLANTING_STATION)
            .withGui(() -> MoreMachineContainerTypes.PLANTING_STATION)
            .withSound(MekanismSounds.ENRICHMENT_CHAMBER)
            .withEnergyConfig(MoreMachineConfig.usage.plantingStation, MoreMachineConfig.storage.plantingStation)
            .with(AttributeUpgradeSupport.DEFAULT_ADVANCED_MACHINE_UPGRADES)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withComputerSupport("plantingStation")
            .build();

    // CNC Stamper
    public static final MoreMachineFactoryMachine<TileEntityStamper> CNC_STAMPER = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.CNC_STAMPER, MoreMachineLang.DESCRIPTION_CNC_STAMPER, MoreMachineFactoryType.CNC_STAMPING)
            .withGui(() -> MoreMachineContainerTypes.CNC_STAMPER)
            .withSound(MekanismSounds.CRUSHER)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_stamper, MoreMachineConfig.storage.cnc_stamper)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_stamper")
            .build();

    // CNC Lathe
    public static final MoreMachineFactoryMachine<TileEntityLathe> CNC_LATHE = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.CNC_LATHE, MoreMachineLang.DESCRIPTION_CNC_LATHE, MoreMachineFactoryType.CNC_LATHING)
            .withGui(() -> MoreMachineContainerTypes.CNC_LATHE)
            .withSound(MekanismSounds.OSMIUM_COMPRESSOR)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_lathe, MoreMachineConfig.storage.cnc_lathe)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_lathe")
            .build();

    // CNC Rolling Mill
    public static final MoreMachineFactoryMachine<TileEntityRollingMill> CNC_ROLLING_MILL = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.CNC_ROLLING_MILL, MoreMachineLang.DESCRIPTION_CNC_ROLLING_MILL, MoreMachineFactoryType.CNC_ROLLING_MILL)
            .withGui(() -> MoreMachineContainerTypes.CNC_ROLLING_MILL)
            .withSound(MekanismSounds.COMBINER)
            .withEnergyConfig(MoreMachineConfig.usage.cnc_rollingMill, MoreMachineConfig.storage.cnc_rollingMill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_rolling_mill")
            .build();

    // Replicator
    public static final MoreMachineFactoryMachine<TileEntityReplicator> REPLICATOR = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.REPLICATOR, MoreMachineLang.DESCRIPTION_REPLICATOR, MoreMachineFactoryType.REPLICATING)
            .withGui(() -> MoreMachineContainerTypes.REPLICATOR)
            .withEnergyConfig(MoreMachineConfig.usage.itemReplicator, MoreMachineConfig.storage.itemReplicator)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withComputerSupport("itemReplicator")
            .build();

    public static final MoreMachineFactoryMachine<TileEntityFluidReplicator> FLUID_REPLICATOR = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.FLUID_REPLICATOR, MoreMachineLang.DESCRIPTION_FLUID_REPLICATOR, MoreMachineFactoryType.REPLICATING)
            .withGui(() -> MoreMachineContainerTypes.FLUID_REPLICATOR)
            .withEnergyConfig(MoreMachineConfig.usage.fluidReplicator, MoreMachineConfig.storage.fluidReplicator)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .withSideConfig(TransmissionType.FLUID, TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withComputerSupport("fluidReplicator")
            .build();

    public static final MoreMachineFactoryMachine<TileEntityChemicalReplicator> CHEMICAL_REPLICATOR = MMMachineBuilder
            .createMMFactoryMachine(() -> MoreMachineTileEntityTypes.CHEMICAL_REPLICATOR, MoreMachineLang.DESCRIPTION_CHEMicAL_REPLICATOR, MoreMachineFactoryType.REPLICATING)
            .withGui(() -> MoreMachineContainerTypes.CHEMIcAL_REPLICATOR)
            .withEnergyConfig(MoreMachineConfig.usage.chemicalReplicator, MoreMachineConfig.storage.chemicalReplicator)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withComputerSupport("chemicalReplicator")
            .build();

    static {
        for (FactoryTier tier : MoreMachineUtils.getFactoryTier()) {
            for (MoreMachineFactoryType type : MoreMachineEnumUtils.MM_FACTORY_TYPES) {
                MM_FACTORIES.put(tier, type, MoreMachineFactoryBuilder.createMoreMachineFactory(() -> MoreMachineTileEntityTypes.getMoreMachineFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    // Ambient Gas Collector
    public static final Machine<TileEntityAmbientGasCollector> AMBIENT_GAS_COLLECTOR = MachineBuilder
            .createMachine(() -> MoreMachineTileEntityTypes.AMBIENT_GAS_COLLECTOR, MoreMachineLang.DESCRIPTION_AMBIENT_GAS_COLLECTOR)
            .withGui(() -> MoreMachineContainerTypes.AMBIENT_GAS_COLLECTOR)
            .withEnergyConfig(MoreMachineConfig.usage.ambientGasCollector, MoreMachineConfig.storage.ambientGasCollector)
            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.ENERGY)
            .withCustomShape(BlockShapes.ELECTRIC_PUMP)
            .withComputerSupport("ambientGasCollector")
            .replace(Attributes.ACTIVE)
            .build();

    // Wireless Charging Station
    public static final Machine<TileEntityWirelessChargingStation> WIRELESS_CHARGING_STATION = MachineBuilder
            .createMachine(() -> MoreMachineTileEntityTypes.WIRELESS_CHARGING_STATION, MoreMachineLang.DESCRIPTION_WIRELESS_CHARGING_STATION)
            .withGui(() -> MoreMachineContainerTypes.WIRELESS_CHARGING_STATION)
            .withEnergyConfig(MoreMachineConfig.storage.wirelessChargingStation)
            // .with(AttributeUpgradeSupport.ANCHOR_ONLY)
            .withSideConfig(TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(MoreMachineBlockShapes.WIRELESS_CHARGING_STATION)
            .with(AttributeCustomSelectionBox.JSON)
            .without(AttributeUpgradeSupport.class)
            .withBounding(new HandleBoundingBlock() {

                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    MutableBlockPos mutable = new MutableBlockPos();
                    for (int i = 0; i < 2; i++) {
                        mutable.setWithOffset(pos, 0, i + 1, 0);
                        if (!consumer.accept(level, mutable, data)) {
                            return false;
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("wirelessChargingStation")
            .replace(Attributes.ACTIVE)
            .build();

    // Wireless Transmission Station
    public static final Machine<TileEntityWirelessTransmissionStation> WIRELESS_TRANSMISSION_STATION = MachineBuilder
            .createMachine(() -> MoreMachineTileEntityTypes.WIRELESS_TRANSMISSION_STATION, MoreMachineLang.DESCRIPTION_WIRELESS_CHARGING_STATION)
            .withGui(() -> MoreMachineContainerTypes.WIRELESS_TRANSMISSION_STATION)
            .withEnergyConfig(MoreMachineConfig.storage.wirelessTransmitterStation)
            .withSideConfig(TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ENERGY, TransmissionType.HEAT)
            .withCustomShape(MoreMachineBlockShapes.WIRELESS_CHARGING_STATION)
            .with(AttributeCustomSelectionBox.JSON)
            .without(AttributeUpgradeSupport.class)
            .withBounding(new HandleBoundingBlock() {

                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    MutableBlockPos mutable = new MutableBlockPos();
                    for (int i = 0; i < 2; i++) {
                        mutable.setWithOffset(pos, 0, i + 1, 0);
                        if (!consumer.accept(level, mutable, data)) {
                            return false;
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("wirelessTransmissionStation")
            .replace(Attributes.ACTIVE)
            .build();

    // Author Doll
    public static final BlockTypeTile<TileEntityDoll> AUTHOR_DOLL = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> MoreMachineTileEntityTypes.AUTHOR_DOLL, MoreMachineLang.AUTHOR_DOLL)
            .with(new AttributeStateFacing(BlockStateProperties.HORIZONTAL_FACING))
            .withCustomShape(MoreMachineBlockShapes.AUTHOR_DOLL)
            .with(AttributeCustomSelectionBox.JSON)
            .build();

    public static MoreMachineFactory<?> getMoreMachineFactory(FactoryTier tier, MoreMachineFactoryType type) {
        return MM_FACTORIES.get(tier, type);
    }
}
