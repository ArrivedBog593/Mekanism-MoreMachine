package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.Mekmm;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class LargeMachineContainerTypes {

    private LargeMachineContainerTypes() {}

    public static final ContainerTypeDeferredRegister LM_CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekmm.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeRotaryCondensentrator>> LARGE_ROTARY_CONDENSENTRATOR = LM_CONTAINER_TYPES.register(LargeMachineBlocks.LARGE_ROTARY_CONDENSENTRATOR, TileEntityLargeRotaryCondensentrator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeElectrolyticSeparator>> LARGE_ELECTROLYTIC_SEPARATOR = LM_CONTAINER_TYPES.register(LargeMachineBlocks.LARGE_ELECTROLYTIC_SEPARATOR, TileEntityLargeElectrolyticSeparator.class);
}
