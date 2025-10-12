package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.block.prefab.MMBlockFactoryMachine;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import com.jerry.mekmm.common.item.block.machine.ItemBlockMoreMachineFactory;
import com.jerry.mekmm.common.tile.TileEntityDoll;
import com.jerry.mekmm.common.tile.factory.*;
import com.jerry.mekmm.common.tile.machine.*;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MoreMachineTileEntityTypes {

    private MoreMachineTileEntityTypes() {
    }

    public static final TileEntityTypeDeferredRegister MM_TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, MoreMachineFactoryType, TileEntityTypeRegistryObject<? extends TileEntityMoreMachineFactory<?>>> MM_FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            registerFactory(tier, MoreMachineFactoryType.RECYCLING, TileEntityRecyclingFactory::new);
            registerFactory(tier, MoreMachineFactoryType.PLANTING_STATION, TileEntityPlantingFactory::new);
            registerFactory(tier, MoreMachineFactoryType.CNC_STAMPING, TileEntityStampingFactory::new);
            registerFactory(tier, MoreMachineFactoryType.CNC_LATHING, TileEntityItemStackToItemStackMMFactory::new);
            registerFactory(tier, MoreMachineFactoryType.CNC_ROLLING_MILL, TileEntityItemStackToItemStackMMFactory::new);
            registerFactory(tier, MoreMachineFactoryType.REPLICATING, TileEntityReplicatingFactory::new);
//            registerFactory(tier, FactoryType.COMBINING, TileEntityCombiningFactory::new);
//            registerFactory(tier, FactoryType.COMPRESSING, TileEntityItemStackChemicalToItemStackFactory::new);
//            registerFactory(tier, FactoryType.CRUSHING, TileEntityItemStackToItemStackFactory::new);
//            registerFactory(tier, FactoryType.ENRICHING, TileEntityItemStackToItemStackFactory::new);
//            registerFactory(tier, FactoryType.INFUSING, TileEntityItemStackChemicalToItemStackFactory::new);
//            registerFactory(tier, FactoryType.INJECTING, TileEntityItemStackChemicalToItemStackFactory::new);
//            registerFactory(tier, FactoryType.PURIFYING, TileEntityItemStackChemicalToItemStackFactory::new);
//            registerFactory(tier, FactoryType.SAWING, TileEntitySawingFactory::new);
//            registerFactory(tier, FactoryType.SMELTING, TileEntityItemStackToItemStackFactory::new);
        }
    }

    private static void registerFactory(FactoryTier tier, MoreMachineFactoryType type, MMBlockEntityFactory<? extends TileEntityMoreMachineFactory<?>> factoryConstructor) {
        BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, ItemBlockMoreMachineFactory> block = MoreMachineBlocks.getMMFactory(tier, type);
        TileEntityTypeRegistryObject<? extends TileEntityMoreMachineFactory<?>> tileRO = MM_TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> factoryConstructor.create(block, pos, state))
                .clientTicker(TileEntityMekanism::tickClient)
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .build();
        MM_FACTORIES.put(tier, type, tileRO);
    }

    public static final TileEntityTypeRegistryObject<TileEntityRecycler> RECYCLER = MM_TILE_ENTITY_TYPES.mekBuilder(MoreMachineBlocks.RECYCLER, TileEntityRecycler::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityPlantingStation> PLANTING_STATION = MM_TILE_ENTITY_TYPES.mekBuilder(MoreMachineBlocks.PLANTING_STATION, TileEntityPlantingStation::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityStamper> CNC_STAMPER = MM_TILE_ENTITY_TYPES.mekBuilder(MoreMachineBlocks.CNC_STAMPER, TileEntityStamper::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLathe> CNC_LATHE = MM_TILE_ENTITY_TYPES.mekBuilder(MoreMachineBlocks.CNC_LATHE, TileEntityLathe::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityRollingMill> CNC_ROLLING_MILL = MM_TILE_ENTITY_TYPES.mekBuilder(MoreMachineBlocks.CNC_ROLLING_MILL, TileEntityRollingMill::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityReplicator> REPLICATOR = MM_TILE_ENTITY_TYPES
            .mekBuilder(MoreMachineBlocks.REPLICATOR, TileEntityReplicator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityFluidReplicator> FLUID_REPLICATOR = MM_TILE_ENTITY_TYPES
            .mekBuilder(MoreMachineBlocks.FLUID_REPLICATOR, TileEntityFluidReplicator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityChemicalReplicator> CHEMICAL_REPLICATOR = MM_TILE_ENTITY_TYPES
            .mekBuilder(MoreMachineBlocks.CHEMICAL_REPLICATOR, TileEntityChemicalReplicator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityAmbientGasCollector> AMBIENT_GAS_COLLECTOR = MM_TILE_ENTITY_TYPES
            .mekBuilder(MoreMachineBlocks.AMBIENT_GAS_COLLECTOR, TileEntityAmbientGasCollector::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIGURABLE)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityDoll> AUTHOR_DOLL = MM_TILE_ENTITY_TYPES
            .builder(MoreMachineBlocks.AUTHOR_DOLL, TileEntityDoll::new)
            .build();

    public static TileEntityTypeRegistryObject<? extends TileEntityMoreMachineFactory<?>> getMMFactoryTile(FactoryTier tier, MoreMachineFactoryType type) {
        return MM_FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static TileEntityTypeRegistryObject<? extends TileEntityMoreMachineFactory<?>>[] getFactoryTiles() {
        return MM_FACTORIES.values().toArray(new TileEntityTypeRegistryObject[0]);
    }

    @FunctionalInterface
    private interface MMBlockEntityFactory<BE extends BlockEntity> {

        BE create(Holder<Block> block, BlockPos pos, BlockState state);
    }
}
