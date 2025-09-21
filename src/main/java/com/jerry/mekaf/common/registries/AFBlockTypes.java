package com.jerry.mekaf.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactory;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine.MMFactoryMachine;
import com.jerry.mekmm.common.content.blocktype.MMMachine.MMMachineBuilder;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.machine.*;
import mekanism.common.util.EnumUtils;

import java.util.EnumSet;

public class AFBlockTypes {

    private AFBlockTypes() {

    }

    private static final Table<FactoryTier, AdvancedFactoryType, AdvancedFactory<?>> AF_FACTORIES = HashBasedTable.create();

    // Chemical Oxidizer
    public static final MMFactoryMachine<TileEntityChemicalOxidizer> CHEMICAL_OXIDIZER = MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_OXIDIZER, MekanismLang.DESCRIPTION_CHEMICAL_OXIDIZER, AdvancedFactoryType.OXIDIZING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_OXIDIZER)
            .withSound(MekanismSounds.CHEMICAL_OXIDIZER)
            .withEnergyConfig(MekanismConfig.usage.oxidationChamber, MekanismConfig.storage.oxidationChamber)
            .withCustomShape(BlockShapes.CHEMICAL_OXIDIZER)
            .withComputerSupport("chemicalOxidizer")
            .build();
    // Chemical Infuser
    public static final MMFactoryMachine<TileEntityChemicalInfuser> CHEMICAL_INFUSER = MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_INFUSER, MekanismLang.DESCRIPTION_CHEMICAL_INFUSER, AdvancedFactoryType.CHEMICAL_INFUSING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_INFUSER)
            .withSound(MekanismSounds.CHEMICAL_INFUSER)
            .withEnergyConfig(MekanismConfig.usage.chemicalInfuser, MekanismConfig.storage.chemicalInfuser)
            .withCustomShape(BlockShapes.CHEMICAL_INFUSER)
            .withComputerSupport("chemicalInfuser")
            .build();
    // Chemical Dissolution Chamber
    public static final MMFactoryMachine<TileEntityChemicalDissolutionChamber> CHEMICAL_DISSOLUTION_CHAMBER = MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_DISSOLUTION_CHAMBER, MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER, AdvancedFactoryType.DISSOLVING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_DISSOLUTION_CHAMBER)
            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER)
            .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, Upgrade.GAS))
            .withCustomShape(BlockShapes.CHEMICAL_DISSOLUTION_CHAMBER)
            .withComputerSupport("chemicalDissolutionChamber")
            .build();
    // Chemical Washer
    public static final MMFactoryMachine<TileEntityChemicalWasher> CHEMICAL_WASHER = MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_WASHER, MekanismLang.DESCRIPTION_CHEMICAL_WASHER, AdvancedFactoryType.WASHING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_WASHER)
            .withSound(MekanismSounds.CHEMICAL_WASHER)
            .withEnergyConfig(MekanismConfig.usage.chemicalWasher, MekanismConfig.storage.chemicalWasher)
            .withCustomShape(BlockShapes.CHEMICAL_WASHER)
            .withComputerSupport("chemicalWasher")
            .build();
    // Isotopic Centrifuge
    public static final MMFactoryMachine<TileEntityIsotopicCentrifuge> ISOTOPIC_CENTRIFUGE = MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.ISOTOPIC_CENTRIFUGE, MekanismLang.DESCRIPTION_ISOTOPIC_CENTRIFUGE, AdvancedFactoryType.CENTRIFUGING)
            .withGui(() -> MekanismContainerTypes.ISOTOPIC_CENTRIFUGE)
            .withEnergyConfig(MekanismConfig.usage.isotopicCentrifuge, MekanismConfig.storage.isotopicCentrifuge)
            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE)
            .withCustomShape(BlockShapes.ISOTOPIC_CENTRIFUGE)
            .withBounding((pos, state, builder) -> builder.add(pos.above()))
            .withComputerSupport("isotopicCentrifuge")
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
