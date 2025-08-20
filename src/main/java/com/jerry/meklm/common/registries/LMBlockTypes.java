package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.content.blocktype.LMBlockShapes;
import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeCustomSelectionBox;
import mekanism.common.block.attribute.AttributeHasBounding;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LMBlockTypes {

    private LMBlockTypes() {

    }

    // Rotary Condensentrator
    public static final Machine<TileEntityLargeRotaryCondensentrator> LARGE_ROTARY_CONDENSENTRATOR = Machine.MachineBuilder
            .createMachine(() -> LMTileEntityTypes.LARGE_ROTARY_CONDENSENTRATOR, MekanismLang.DESCRIPTION_ROTARY_CONDENSENTRATOR)
            .withGui(() -> LMContainerTypes.LARGE_ROTARY_CONDENSENTRATOR)
            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR)
            .withEnergyConfig(MekanismConfig.usage.rotaryCondensentrator, MekanismConfig.storage.rotaryCondensentrator)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(LMBlockShapes.LARGE_ROTARY_CONDENSENTRATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> predicate) {
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
            })
            .withComputerSupport("largeRotaryCondensentrator")
            .build();
}
