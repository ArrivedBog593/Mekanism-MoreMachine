package com.jerry.mekmm.common.block.attribute;

import mekanism.common.block.attribute.AttributeHasBounding;
import mekanism.common.block.attribute.AttributeHasBounding.HandleBoundingBlock;
import mekanism.common.block.attribute.AttributeHasBounding.TriBooleanFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MoreMachineAttributeHasBounding {

    public static final AttributeHasBounding FULL_JAVA_ENTITY = new AttributeHasBounding(new HandleBoundingBlock() {

        @Override
        public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> predicate) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 2; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || y != 0 || z != 0) {
                            mutable.setWithOffset(pos, x, y, z);
                            if (!predicate.accept(level, mutable, data)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    });
}
