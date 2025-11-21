package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.content.blocktype.LargeMachineBlockShapes;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.common.config.MoreMachineConfig;

import mekanism.api.math.MathUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeCustomSelectionBox;
import mekanism.common.block.attribute.AttributeHasBounding.HandleBoundingBlock;
import mekanism.common.block.attribute.AttributeHasBounding.TriBooleanFunction;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.util.ChemicalUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LargeMachineBlockTypes {

    // Rotary Condensentrator
    public static final Machine<TileEntityLargeRotaryCondensentrator> LARGE_ROTARY_CONDENSENTRATOR = MachineBuilder
            .createMachine(() -> LargeMachineTileEntityTypes.LARGE_ROTARY_CONDENSENTRATOR, MekanismLang.DESCRIPTION_ROTARY_CONDENSENTRATOR)
            .withGui(() -> LargeMachineContainerTypes.LARGE_ROTARY_CONDENSENTRATOR)
            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR)
            .withEnergyConfig(MoreMachineConfig.usage.largeRotaryCondensentrator, MoreMachineConfig.storage.largeRotaryCondensentrator)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(LargeMachineBlockShapes.LARGE_ROTARY_CONDENSENTRATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new HandleBoundingBlock() {

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
            })
            .withComputerSupport("largeRotaryCondensentrator")
            .build();

    // Electrolytic Separator
    public static final Machine<TileEntityLargeElectrolyticSeparator> LARGE_ELECTROLYTIC_SEPARATOR = MachineBuilder
            .createMachine(() -> LargeMachineTileEntityTypes.LARGE_ELECTROLYTIC_SEPARATOR, MekanismLang.DESCRIPTION_ELECTROLYTIC_SEPARATOR)
            .withGui(() -> LargeMachineContainerTypes.LARGE_ELECTROLYTIC_SEPARATOR)
            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR)
            .withEnergyConfig(() -> MathUtils.multiplyClamped(2, ChemicalUtil.hydrogenEnergyDensity()), MekanismConfig.storage.electrolyticSeparator)
            .withSideConfig(TransmissionType.FLUID, TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(LargeMachineBlockShapes.LARGE_ELECTROLYTIC_SEPARATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new HandleBoundingBlock() {

                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!consumer.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("largeElectrolyticSeparator")
            .build();

    private LargeMachineBlockTypes() {}
}
