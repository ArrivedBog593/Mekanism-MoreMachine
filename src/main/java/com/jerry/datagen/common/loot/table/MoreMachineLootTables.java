package com.jerry.datagen.common.loot.table;

import com.jerry.mekaf.common.registries.AdvancedFactoryBlocks;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import net.minecraft.core.HolderLookup;

public class MoreMachineLootTables extends BaseBlockLootTables {

    public MoreMachineLootTables(HolderLookup.Provider provider) {
        super(provider);
    }

    @Override
    protected void generate() {
        dropSelfWithContents(MoreMachineBlocks.MM_BLOCKS.getPrimaryEntries());
        dropSelfWithContents(AdvancedFactoryBlocks.AF_BLOCKS.getPrimaryEntries());
    }
}