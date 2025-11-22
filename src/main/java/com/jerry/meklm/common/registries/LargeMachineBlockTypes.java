package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.content.blocktype.LargeMachineBlockShapes;
import com.jerry.meklm.common.tile.generator.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.generator.TileEntityLargeHeatGenerator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.common.block.attribute.MoreMachineAttributeHasBounding;
import com.jerry.mekmm.common.config.MoreMachineConfig;

import mekanism.api.math.MathUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeCustomSelectionBox;
import mekanism.common.block.attribute.AttributeHasBounding.HandleBoundingBlock;
import mekanism.common.block.attribute.AttributeHasBounding.TriBooleanFunction;
import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.util.ChemicalUtil;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
            .with(MoreMachineAttributeHasBounding.FULL_JAVA_ENTITY)
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

    // Heat Generator
    public static final Generator<TileEntityLargeHeatGenerator> LARGE_HEAT_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> LargeMachineTileEntityTypes.LARGE_HEAT_GENERATOR, GeneratorsLang.DESCRIPTION_HEAT_GENERATOR)
            .withGui(() -> LargeMachineContainerTypes.LARGE_HEAT_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.heatGenerator)
            .withCustomShape(LargeMachineBlockShapes.LARGE_HEAT_GENERATOR)
            .withSound(GeneratorsSounds.HEAT_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .with(AttributeCustomSelectionBox.JSON)
            .with(MoreMachineAttributeHasBounding.FULL_JAVA_ENTITY)
            .withComputerSupport("largeHeatGenerator")
            .replace(Attributes.ACTIVE_MELT_LIGHT)
            .with(new AttributeParticleFX()
                    .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52))
                    .add(ParticleTypes.FLAME, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52)))
            .build();

    // Gas Burning Generator
    public static final Generator<TileEntityLargeGasGenerator> LARGE_GAS_BURNING_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> LargeMachineTileEntityTypes.LARGE_GAS_BURNING_GENERATOR, GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR)
            .withGui(() -> LargeMachineContainerTypes.LARGE_GAS_BURNING_GENERATOR)
            .withEnergyConfig(() -> MathUtils.multiplyClamped(1_000, ChemicalUtil.hydrogenEnergyDensity()))
            .withCustomShape(LargeMachineBlockShapes.LARGE_GAS_BURNING_GENERATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .with(MoreMachineAttributeHasBounding.FULL_JAVA_ENTITY)
            .withComputerSupport("largeGasBurningGenerator")
            .replace(Attributes.ACTIVE_MELT_LIGHT)
            .build();

    private LargeMachineBlockTypes() {}
}
