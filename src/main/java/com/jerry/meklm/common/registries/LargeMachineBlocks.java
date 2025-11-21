package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;

import com.jerry.mekmm.Mekmm;

import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
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

public class LargeMachineBlocks {

    private LargeMachineBlocks() {}

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
}
