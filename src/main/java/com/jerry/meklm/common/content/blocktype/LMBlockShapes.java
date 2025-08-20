package com.jerry.meklm.common.content.blocktype;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LMBlockShapes {

    private LMBlockShapes() {

    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static final VoxelShape[] LARGE_ROTARY_CONDENSENTRATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(-16, 3, 3, -15, 13, 13),
                box(-13.75, 3, 3, -12.75, 13, 13),
                box(2, -13, 31, 12, -3, 32),
                box(31, 3, 3, 32, 13, 13),
                box(28.5, 3, 3, 29.5, 13, 13),
                box(-15, -16, -15, 31, -11, 31),
                box(24, 26, 24, 29, 29, 29),
                box(24, 26, -13, 29, 29, -8),
                box(-13, 26, -13, -8, 29, -8),
                box(-13, 26, 24, -8, 29, 29),
                box(-4, 26, -4, 20, 29, 20),
                box(-15, 23, -15, 31, 26, 31),
                box(-15, 29, -15, 31, 32, 31),
                box(11, -11, -13, 29, 23, 29),
                box(-13, -11, -13, 5, 23, 29),
                box(5, -11, -6.875, 11, 20, 22.125),
                box(6.5, 20, -5.875, 9.5, 23, -2.875),
                box(5, -9.5, -12.875, 11, -4.5, -7.875),
                box(5, 16.5, 23.125, 11, 21.5, 28.125),
                box(5, 10, 23.125, 11, 15, 28.125),
                box(5, 3.5, 23.125, 11, 8.5, 28.125),
                box(5, -3, 23.125, 11, 2, 28.125),
                box(5, -3, -12.875, 11, 2, -7.875),
                box(5, 3.5, -12.875, 11, 8.5, -7.875),
                box(-15, 4, 4, -9, 12, 12),
                box(25, 4, 4, 31, 12, 12),
                box(5, 10, -12.875, 11, 15, -7.875),
                box(5, 16.5, -12.875, 11, 21.5, -7.875),
                box(6.5, 20, 6.125, 9.5, 23, 9.125),
                box(6.5, 20, 2.125, 9.5, 23, 5.125),
                box(6.5, 20, 14.125, 9.5, 23, 17.125),
                box(6.5, 20, 18.125, 9.5, 23, 21.125),
                box(6.5, 20, 10.125, 9.5, 23, 13.125),
                box(6.5, 20, -1.875, 9.5, 23, 1.125),
                box(3, -12, 22, 11, -4, 31),
                box(19, -13, 31, 29, -3, 32),
                box(20, -12, 25, 28, -4, 31),
                box(-13, -13, 31, -3, -3, 32),
                box(-12, -12, 25, -4, -4, 31)
        ).move(0, 1, 0), LARGE_ROTARY_CONDENSENTRATOR);
    }
}
