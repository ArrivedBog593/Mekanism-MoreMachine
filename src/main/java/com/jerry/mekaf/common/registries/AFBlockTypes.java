package com.jerry.mekaf.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactory;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.machine.*;
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

    // Chemical Infuser
    public static final MMMachine.MMFactoryMachine<TileEntityChemicalInfuser> CHEMICAL_INFUSER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_INFUSER, MekanismLang.DESCRIPTION_CHEMICAL_INFUSER, AdvancedFactoryType.CHEMICAL_INFUSING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_INFUSER)
            .withSound(MekanismSounds.CHEMICAL_INFUSER)
            .withEnergyConfig(MekanismConfig.usage.chemicalInfuser, MekanismConfig.storage.chemicalInfuser)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(BlockShapes.CHEMICAL_INFUSER)
            .withComputerSupport("chemicalInfuser")
            .build();

    // Chemical Washer
    public static final MMMachine.MMFactoryMachine<TileEntityChemicalWasher> CHEMICAL_WASHER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_WASHER, MekanismLang.DESCRIPTION_CHEMICAL_WASHER, AdvancedFactoryType.WASHING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_WASHER)
            .withSound(MekanismSounds.CHEMICAL_WASHER)
            .withEnergyConfig(MekanismConfig.usage.chemicalWasher, MekanismConfig.storage.chemicalWasher)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(BlockShapes.CHEMICAL_WASHER)
            .withComputerSupport("chemicalWasher")
            .build();

    // Pressurized Reaction Chamber
    public static final MMMachine.MMFactoryMachine<TileEntityPressurizedReactionChamber> PRESSURIZED_REACTION_CHAMBER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.PRESSURIZED_REACTION_CHAMBER, MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER, AdvancedFactoryType.PRESSURISED_REACTING)
            .withGui(() -> MekanismContainerTypes.PRESSURIZED_REACTION_CHAMBER)
            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase, MekanismConfig.storage.pressurizedReactionBase)
            .withSideConfig(TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ENERGY)
            .withCustomShape(BlockShapes.PRESSURIZED_REACTION_CHAMBER)
            .withComputerSupport("pressurizedReactionChamber")
            .build();

    // Chemical Crystallizer
    public static final MMMachine.MMFactoryMachine<TileEntityChemicalCrystallizer> CHEMICAL_CRYSTALLIZER = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.CHEMICAL_CRYSTALLIZER, MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER, AdvancedFactoryType.CRYSTALLIZING)
            .withGui(() -> MekanismContainerTypes.CHEMICAL_CRYSTALLIZER)
            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER)
            .withEnergyConfig(MekanismConfig.usage.chemicalCrystallizer, MekanismConfig.storage.chemicalCrystallizer)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withCustomShape(BlockShapes.CHEMICAL_CRYSTALLIZER)
            .withComputerSupport("chemicalCrystallizer")
            .build();

    // Isotopic Centrifuge
    public static final MMMachine.MMFactoryMachine<TileEntityIsotopicCentrifuge> ISOTOPIC_CENTRIFUGE = MMMachine.MMMachineBuilder
            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.ISOTOPIC_CENTRIFUGE, MekanismLang.DESCRIPTION_ISOTOPIC_CENTRIFUGE, AdvancedFactoryType.CENTRIFUGING)
            .withGui(() -> MekanismContainerTypes.ISOTOPIC_CENTRIFUGE)
            .withEnergyConfig(MekanismConfig.usage.isotopicCentrifuge, MekanismConfig.storage.isotopicCentrifuge)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE)
            .withCustomShape(BlockShapes.ISOTOPIC_CENTRIFUGE)
            .with(AttributeHasBounding.ABOVE_ONLY)
            .withComputerSupport("isotopicCentrifuge")
            .build();

    // Solar Neutron Activator
//    public static final MMMachine.MMFactoryMachine<TileEntitySolarNeutronActivator> SOLAR_NEUTRON_ACTIVATOR = MMMachine.MMMachineBuilder
//            .createAdvancedFactoryMachine(() -> MekanismTileEntityTypes.SOLAR_NEUTRON_ACTIVATOR, MekanismLang.DESCRIPTION_SOLAR_NEUTRON_ACTIVATOR, AdvancedFactoryType.SOLAR_NEUTRON_ACTIVATING)
//            .withGui(() -> MekanismContainerTypes.SOLAR_NEUTRON_ACTIVATOR)
//            .without(AttributeParticleFX.class, AttributeUpgradeSupport.class)
//            .withCustomShape(BlockShapes.SOLAR_NEUTRON_ACTIVATOR)
//            .with(AttributeCustomSelectionBox.JSON)
//            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ITEM)
//            .with(AttributeHasBounding.ABOVE_ONLY)
//            .withComputerSupport("solarNeutronActivator")
//            .replace(Attributes.ACTIVE)
//            .build();

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
