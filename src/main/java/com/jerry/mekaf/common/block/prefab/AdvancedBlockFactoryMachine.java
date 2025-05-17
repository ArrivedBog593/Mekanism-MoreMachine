package com.jerry.mekaf.common.block.prefab;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactory;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekaf.common.tile.factory.TileEntityAdvancedFactoryBase;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.states.IStateFluidLoggable;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.base.TileEntityMekanism;

import java.util.function.UnaryOperator;

public class AdvancedBlockFactoryMachine<TILE extends TileEntityMekanism, MACHINE extends MMMachine.MMFactoryMachine<TILE>> extends BlockTile<TILE, MACHINE> {

    public AdvancedBlockFactoryMachine(MACHINE machine, UnaryOperator<Properties> propertiesModifier) {
        super(machine, propertiesModifier);
    }

    public static class MMBlockFactoryMachineModel<TILE extends TileEntityMekanism, MACHINE extends MMMachine.MMFactoryMachine<TILE>> extends AdvancedBlockFactoryMachine<TILE, MACHINE> implements IStateFluidLoggable {

        public MMBlockFactoryMachineModel(MACHINE machineType, UnaryOperator<Properties> propertiesModifier) {
            super(machineType, propertiesModifier);
        }
    }

    public static class AdvancedBlockFactory<TILE extends TileEntityAdvancedFactoryBase<?>> extends MMBlockFactoryMachineModel<TILE, AdvancedFactory<TILE>> {

        public AdvancedBlockFactory(AdvancedFactory<TILE> factoryType) {
            super(factoryType, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
        }
    }
}
