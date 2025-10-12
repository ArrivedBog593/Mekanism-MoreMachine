package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;
import com.jerry.mekmm.Mekmm;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.RotaryInputRecipeCache;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.resource.BlockResourceInfo;

public class LargeMachineBlocks {

    private LargeMachineBlocks() {

    }

    public static final BlockDeferredRegister LM_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityLargeRotaryCondensentrator, Machine<TileEntityLargeRotaryCondensentrator>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityLargeRotaryCondensentrator, Machine<TileEntityLargeRotaryCondensentrator>>>> LARGE_ROTARY_CONDENSENTRATOR =
            LM_BLOCKS.register("large_rotary_condensentrator", () -> new BlockTile.BlockTileModel<>(LargeMachineBlockTypes.LARGE_ROTARY_CONDENSENTRATOR,
                    properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                    .component(MekanismDataComponents.ROTARY_MODE, false)
            )).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityLargeRotaryCondensentrator.CAPACITY * 100, MekanismRecipeType.ROTARY, RotaryInputRecipeCache::containsInput)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityLargeRotaryCondensentrator.CAPACITY * 100, MekanismRecipeType.ROTARY, RotaryInputRecipeCache::containsInput)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addChemicalRotaryDrainSlot(0)
                            .addChemicalRotaryFillSlot(0)
                            .addFluidRotarySlot(0)
                            .addOutput()
                            .addEnergy()
                            .build()
                    )
            );
}
