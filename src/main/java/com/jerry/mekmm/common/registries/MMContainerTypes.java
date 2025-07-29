package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.inventory.container.tile.MMFactoryContainer;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import com.jerry.mekmm.common.tile.machine.TileEntityRecycler;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class MMContainerTypes {

    private MMContainerTypes() {

    }

    public static final ContainerTypeDeferredRegister MM_CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekmm.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRecycler>> RECYCLER = MM_CONTAINER_TYPES.register(MMBlocks.RECYCLER, TileEntityRecycler.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<MMTileEntityFactory<?>>> MM_FACTORY = MM_CONTAINER_TYPES.register("factory", factoryClass(), MMFactoryContainer::new);

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Class<MMTileEntityFactory<?>> factoryClass() {
        return (Class) MMTileEntityFactory.class;
    }
}
