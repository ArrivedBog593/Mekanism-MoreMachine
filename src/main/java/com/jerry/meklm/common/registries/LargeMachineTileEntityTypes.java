package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.generator.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.generator.TileEntityLargeHeatGenerator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.Mekmm;

import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class LargeMachineTileEntityTypes {

    private LargeMachineTileEntityTypes() {}

    public static final TileEntityTypeDeferredRegister LM_TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekmm.MOD_ID);

    public static final TileEntityTypeRegistryObject<TileEntityLargeRotaryCondensentrator> LARGE_ROTARY_CONDENSENTRATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LargeMachineBlocks.LARGE_ROTARY_CONDENSENTRATOR, TileEntityLargeRotaryCondensentrator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.CHEMICAL.block(), Capabilities.FLUID.block(), Capabilities.ITEM.block(), Capabilities.ENERGY.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeElectrolyticSeparator> LARGE_ELECTROLYTIC_SEPARATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LargeMachineBlocks.LARGE_ELECTROLYTIC_SEPARATOR, TileEntityLargeElectrolyticSeparator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.CHEMICAL.block(), Capabilities.FLUID.block(), Capabilities.ITEM.block(), Capabilities.ENERGY.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeHeatGenerator> LARGE_HEAT_GENERATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LargeMachineBlocks.LARGE_HEAT_GENERATOR, TileEntityLargeHeatGenerator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeGasGenerator> LARGE_GAS_BURNING_GENERATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LargeMachineBlocks.LARGE_GAS_BURNING_GENERATOR, TileEntityLargeGasGenerator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
}
