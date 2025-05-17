package com.jerry.mekaf.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactory;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeSideConfig;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.machine.TileEntityChemicalDissolutionChamber;
import mekanism.common.tile.machine.TileEntityChemicalOxidizer;
import mekanism.common.util.EnumUtils;

public class AFBlockTypes {

    private AFBlockTypes() {
    }

    private static final Table<FactoryTier, AdvancedFactoryType, AdvancedFactory<?>> AF_FACTORIES = HashBasedTable.create();

    // Chemical Oxidizer
    public static final MMMachine.MMFactoryMachine<TileEntityChemicalOxidizer> CHEMICAL_OXIDIZER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_OXIDIZER, MekanismLang.DESCRIPTION_CHEMICAL_OXIDIZER, AdvancedFactoryType.OXIDIZING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_OXIDIZER)
            .withSound(MekanismSounds.CHEMICAL_OXIDIZER)
            .withEnergyConfig(MekanismConfig.usage.chemicalOxidizer, MekanismConfig.storage.chemicalOxidizer)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withCustomShape(BlockShapes.CHEMICAL_OXIDIZER)
            .withComputerSupport("chemicalOxidizer")
            .build();

    // Chemical Dissolution Chamber
    public static final MMMachine.MMFactoryMachine<TileEntityChemicalDissolutionChamber> CHEMICAL_DISSOLUTION_CHAMBER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_DISSOLUTION_CHAMBER, MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER, AdvancedFactoryType.DISSOLVING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_DISSOLUTION_CHAMBER)
            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER)
            .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
            .with(AttributeUpgradeSupport.DEFAULT_ADVANCED_MACHINE_UPGRADES)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withCustomShape(BlockShapes.CHEMICAL_DISSOLUTION_CHAMBER)
            .withComputerSupport("chemicalDissolutionChamber")
            .build();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (AdvancedFactoryType type : MMEnumUtils.ADVANCED_FACTORY_TYPES) {
                AF_FACTORIES.put(tier, type, AdvancedFactory.AdvancedFactoryBuilder.createAdvancedFactory(() -> AFTileEntityTypes.getAdvancedFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    public static AdvancedFactory<?> getAdvancedFactory(FactoryTier tier, AdvancedFactoryType type) {
        return AF_FACTORIES.get(tier, type);
    }
}
