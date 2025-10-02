package com.jerry.mekaf.common.content.blocktype;

import com.jerry.mekaf.common.registries.AFBlockTypes;
import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.content.blocktype.MMMachine.MMFactoryMachine;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;

import java.util.Locale;
import java.util.function.Supplier;

@NothingNullByDefault
public enum AdvancedFactoryType implements IHasTranslationKey {
    OXIDIZING("oxidizing", MMLang.OXIDIZING, () -> AFBlockTypes.CHEMICAL_OXIDIZER, () -> MekanismBlocks.CHEMICAL_OXIDIZER),
    CHEMICAL_INFUSING("chemical_infusing", MMLang.CHEMICAL_INFUSING, () -> AFBlockTypes.CHEMICAL_INFUSER, () -> MekanismBlocks.CHEMICAL_INFUSER),

    DISSOLVING("dissolving", MMLang.DISSOLVING, () -> AFBlockTypes.CHEMICAL_DISSOLUTION_CHAMBER, () -> MekanismBlocks.CHEMICAL_DISSOLUTION_CHAMBER),
    WASHING("washing", MMLang.WASHING, () -> AFBlockTypes.CHEMICAL_WASHER, () -> MekanismBlocks.CHEMICAL_WASHER),
    CRYSTALLIZING("crystallizing", MMLang.CRYSTALLIZING, () -> AFBlockTypes.CHEMICAL_CRYSTALLIZER, () -> MekanismBlocks.CHEMICAL_CRYSTALLIZER),
    PRESSURISED_REACTING("pressurised_reacting", MMLang.PRESSURISED_REACTING, () -> AFBlockTypes.PRESSURIZED_REACTION_CHAMBER, () -> MekanismBlocks.PRESSURIZED_REACTION_CHAMBER),
    CENTRIFUGING("centrifuging", MMLang.CENTRIFUGING, () -> AFBlockTypes.ISOTOPIC_CENTRIFUGE, () -> MekanismBlocks.ISOTOPIC_CENTRIFUGE),
    LIQUIFYING("liquifying", MMLang.LIQUIFYING, () -> AFBlockTypes.NUTRITIONAL_LIQUIFIER, () -> MekanismBlocks.NUTRITIONAL_LIQUIFIER);

    private final String registryNameComponent;
    private final ILangEntry langEntry;
    private final Supplier<MMFactoryMachine<?>> baseMachine;
    private final Supplier<BlockRegistryObject<?, ?>> baseBlock;

    AdvancedFactoryType(String registryNameComponent, ILangEntry langEntry, Supplier<MMFactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
        this.registryNameComponent = registryNameComponent;
        this.langEntry = langEntry;
        this.baseMachine = baseMachine;
        this.baseBlock = baseBlock;
    }

    public String getRegistryNameComponent() {
        return registryNameComponent;
    }

    public String getRegistryNameComponentCapitalized() {
        String name = getRegistryNameComponent();
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public MMFactoryMachine<?> getBaseMachine() {
        return baseMachine.get();
    }

    public BlockRegistryObject<?, ?> getBaseBlock() {
        return baseBlock.get();
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }
}
