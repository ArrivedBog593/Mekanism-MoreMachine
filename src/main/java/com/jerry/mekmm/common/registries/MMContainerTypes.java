package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.inventory.container.tile.MMFactoryContainer;
import com.jerry.mekmm.common.tile.factory.TileEntityMMFactory;
import com.jerry.mekmm.common.tile.machine.*;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class MMContainerTypes {

    private MMContainerTypes() {

    }

    public static final ContainerTypeDeferredRegister MM_CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekmm.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRecycler>> RECYCLER = MM_CONTAINER_TYPES.register(com.jerry.mekmm.common.registries.MMBlocks.RECYCLER, TileEntityRecycler.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityPlantingStation>> PLANTING_STATION = MM_CONTAINER_TYPES.register(com.jerry.mekmm.common.registries.MMBlocks.PLANTING_STATION, TileEntityPlantingStation.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityStamper>> CNC_STAMPER = MM_CONTAINER_TYPES.register(com.jerry.mekmm.common.registries.MMBlocks.CNC_STAMPER, TileEntityStamper.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLathe>> CNC_LATHE = MM_CONTAINER_TYPES.register(com.jerry.mekmm.common.registries.MMBlocks.CNC_LATHE, TileEntityLathe.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRollingMill>> CNC_ROLLING_MILL = MM_CONTAINER_TYPES.register(com.jerry.mekmm.common.registries.MMBlocks.CNC_ROLLING_MILL, TileEntityRollingMill.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityReplicator>> REPLICATOR = MM_CONTAINER_TYPES.register(MMBlocks.REPLICATOR, TileEntityReplicator.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityMMFactory<?>>> MM_FACTORY = MM_CONTAINER_TYPES.register("factory", factoryClass(), MMFactoryContainer::new);

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Class<TileEntityMMFactory<?>> factoryClass() {
        return (Class) TileEntityMMFactory.class;
    }
}
