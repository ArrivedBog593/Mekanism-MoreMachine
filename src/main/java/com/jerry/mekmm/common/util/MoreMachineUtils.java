package com.jerry.mekmm.common.util;

import com.jerry.mekmm.common.block.attribute.AttributeMoreMachineFactoryType;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import com.jerry.mekmm.common.registries.MoreMachineTileEntityTypes;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MoreMachineUtils {

    //从MekanismUtils的isSameTypeFactory单拎出来的
    public static boolean isSameMMTypeFactory(Block block, BlockEntityType<?> factoryTileType) {
        return Attribute.matches(block, AttributeMoreMachineFactoryType.class, attribute -> {
            MoreMachineFactoryType factoryType = attribute.getMMFactoryType();
            //Check all factory types
            for (FactoryTier factoryTier : EnumUtils.FACTORY_TIERS) {
                if (MoreMachineTileEntityTypes.getMoreMachineFactoryTile(factoryTier, factoryType).get() == factoryTileType) {
                    return true;
                }
            }
            return false;
        });
    }
}
