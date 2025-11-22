package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.generator.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.generator.TileEntityLargeHeatGenerator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.machine.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.Mekmm;

import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.heat.HeatCapacitorsBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.RotaryInputRecipeCache;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.Generator;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.MapColor;

public class LargeMachineBlocks {

    public static final BlockDeferredRegister LM_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);
    public static final BlockRegistryObject<BlockTileModel<TileEntityLargeRotaryCondensentrator, Machine<TileEntityLargeRotaryCondensentrator>>, ItemBlockTooltip<BlockTileModel<TileEntityLargeRotaryCondensentrator, Machine<TileEntityLargeRotaryCondensentrator>>>> LARGE_ROTARY_CONDENSENTRATOR = LM_BLOCKS.register("large_rotary_condensentrator", () -> new BlockTileModel<>(LargeMachineBlockTypes.LARGE_ROTARY_CONDENSENTRATOR,
            properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
            (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                    .component(MekanismDataComponents.ROTARY_MODE, false)))
            .forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityLargeRotaryCondensentrator.CAPACITY * 100, MekanismRecipeType.ROTARY, RotaryInputRecipeCache::containsInput)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityLargeRotaryCondensentrator.CAPACITY * 100, MekanismRecipeType.ROTARY, RotaryInputRecipeCache::containsInput)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addChemicalRotaryDrainSlot(0)
                            .addChemicalRotaryFillSlot(0)
                            .addFluidRotarySlot(0)
                            .addOutput()
                            .addEnergy()
                            .build()));
    public static final BlockRegistryObject<BlockTileModel<TileEntityLargeElectrolyticSeparator, Machine<TileEntityLargeElectrolyticSeparator>>, ItemBlockTooltip<BlockTileModel<TileEntityLargeElectrolyticSeparator, Machine<TileEntityLargeElectrolyticSeparator>>>> LARGE_ELECTROLYTIC_SEPARATOR = LM_BLOCKS.register("large_electrolytic_separator", () -> new BlockTileModel<>(LargeMachineBlockTypes.LARGE_ELECTROLYTIC_SEPARATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
            (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                    .component(MekanismDataComponents.DUMP_MODE, TileEntityChemicalTank.GasMode.IDLE)
                    .component(MekanismDataComponents.SECONDARY_DUMP_MODE, TileEntityChemicalTank.GasMode.IDLE)
                    .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                    .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.SEPARATOR)))
            .forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_FLUID, MekanismRecipeType.SEPARATING, InputRecipeCache.SingleFluid::containsInput)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_GAS)
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_GAS)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addFluidFillSlot(0)
                            .addChemicalDrainSlot(0)
                            .addChemicalDrainSlot(1)
                            .addEnergy()
                            .build()));

    // Generator
    public static final BlockRegistryObject<BlockTileModel<TileEntityLargeHeatGenerator, Generator<TileEntityLargeHeatGenerator>>, ItemBlockTooltip<BlockTileModel<TileEntityLargeHeatGenerator, Generator<TileEntityLargeHeatGenerator>>>> LARGE_HEAT_GENERATOR = LM_BLOCKS.registerDetails("large_heat_generator", () -> new BlockTileModel<>(LargeMachineBlockTypes.LARGE_HEAT_GENERATOR, properties -> properties.mapColor(MapColor.METAL)))
            .forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(MekanismGeneratorsConfig.generators.heatTankCapacity, fluid -> fluid.is(FluidTags.LAVA))
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.HEAT, () -> HeatCapacitorsBuilder.builder()
                            .addBasic(TileEntityLargeHeatGenerator.HEAT_CAPACITY, TileEntityLargeHeatGenerator.INVERSE_CONDUCTION_COEFFICIENT, TileEntityLargeHeatGenerator.INVERSE_INSULATION_COEFFICIENT)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addFluidFuelSlot(0, s -> s.getBurnTime(null) != 0)
                            .addEnergy()
                            .build()));

    public static final BlockRegistryObject<BlockTileModel<TileEntityLargeGasGenerator, Generator<TileEntityLargeGasGenerator>>, ItemBlockTooltip<BlockTileModel<TileEntityLargeGasGenerator, Generator<TileEntityLargeGasGenerator>>>> LARGE_GAS_BURNING_GENERATOR = LM_BLOCKS.registerDetails("large_gas_burning_generator", () -> new BlockTileModel<>(LargeMachineBlockTypes.LARGE_GAS_BURNING_GENERATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())))
            .forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(MekanismGeneratorsConfig.generators.gbgTankCapacity, TileEntityLargeGasGenerator.HAS_FUEL)
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addChemicalFillSlot(0)
                            .addEnergy()
                            .build()));

    private LargeMachineBlocks() {}
}
