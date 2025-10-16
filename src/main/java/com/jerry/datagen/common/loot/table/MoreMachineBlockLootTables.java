package com.jerry.datagen.common.loot.table;

import com.jerry.mekaf.common.registries.AFBlocks;
import com.jerry.mekmm.common.registries.MMBlocks;

public class MoreMachineBlockLootTables extends BaseBlockLootTables {

    @Override
    protected void generate() {
        dropSelfWithContents(MMBlocks.MM_BLOCKS.getAllBlocks());
        dropSelfWithContents(AFBlocks.AF_BLOCKS.getAllBlocks());
    }
}