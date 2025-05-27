package com.jerry.datagen.common.loot.table;

import com.jerry.mekaf.common.registries.AFBlocks;
import com.jerry.mekmm.common.registries.MMBlocks;
import net.minecraft.core.HolderLookup;

public class MoreMachineLootTables extends BaseBlockLootTables {

    public MoreMachineLootTables(HolderLookup.Provider provider) {
        super(provider);
    }

    @Override
    protected void generate() {
        dropSelfWithContents(MMBlocks.MM_BLOCKS.getPrimaryEntries());
        dropSelfWithContents(AFBlocks.AF_BLOCKS.getPrimaryEntries());
    }
}