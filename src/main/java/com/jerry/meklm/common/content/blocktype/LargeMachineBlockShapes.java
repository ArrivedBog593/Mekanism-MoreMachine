package com.jerry.meklm.common.content.blocktype;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LargeMachineBlockShapes {

    public static final VoxelShape[] LARGE_ROTARY_CONDENSENTRATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(

                box(-12, -12, 32, -4, -4, 32),
                box(4, -12, 32, 12, -4, 32),
                box(20, -12, 32, 28, -4, 32),
                box(-16, 3, 3, -16, 13, 13),
                box(32, 3, 3, 32, 13, 13),
                box(14, 7, -15, 26, 15, -15),
                box(-12, -12, 31, -4, -4, 32),
                box(-11, -11, 29, -5, -5, 31),
                box(4, -12, 31, 12, -4, 32),
                box(5, -11, 23, 11, -5, 31),
                box(20, -12, 31, 28, -4, 32),
                box(21, -11, 29, 27, -5, 31),
                box(-16, 3, 3, -15, 13, 13),
                box(-15, 4, 4, -13, 12, 12),
                box(31, 3, 3, 32, 13, 13),
                box(29, 4, 4, 31, 12, 12),
                box(-15, -16, -15, 31, -11, 31),
                box(-13, -11, -13, 5, 23, 29),
                box(11, -11, -13, 29, 23, 29),
                box(5, -11, -7, 11, 16, 23),
                box(6.5, 16, 14.5, 9.5, 23, 17.5),
                box(6.5, 16, -1.5, 9.5, 23, 1.5),
                box(5, 18, -12, 11, 21, -9),
                box(5, 13, -12, 11, 16, -9),
                box(5, 8, -12, 11, 11, -9),
                box(5, 3, -12, 11, 6, -9),
                box(5, -2, -12, 11, 1, -9),
                box(5, 18, 25, 11, 21, 28),
                box(5, 13, 25, 11, 16, 28),
                box(5, 8, 25, 11, 11, 28),
                box(5, 3, 25, 11, 6, 28),
                //这里删除了一段负尺寸模型
                box(15, 4, -16, 25, 5, -11),
                box(13, 6, -15, 27, 16, -13),
                box(24, 26, 24, 29, 29, 29),
                box(24, 26, -13, 29, 29, -8),
                box(-13, 26, -13, -8, 29, -8),
                box(-13, 26, 24, -8, 29, 29),
                box(-4, 26, -4, 20, 29, 20),
                box(-15, 23, -15, 31, 26, 31),
                box(-15, 29, -15, 31, 32, 31)
        ).move(0, 1, 0), LARGE_ROTARY_CONDENSENTRATOR);
    }

    private LargeMachineBlockShapes() {
    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
