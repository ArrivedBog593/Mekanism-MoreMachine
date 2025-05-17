package com.jerry.mekaf.common.content.blocktype;

import com.jerry.mekaf.common.registries.AFBlockTypes;
import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;

import java.util.Locale;
import java.util.function.Supplier;

@NothingNullByDefault
public enum AdvancedFactoryType implements IHasTranslationKey.IHasEnumNameTranslationKey {
    OXIDIZING("oxidizing", MMLang.AUTHOR_DOLL, () -> AFBlockTypes.CHEMICAL_OXIDIZER, () -> MekanismBlocks.CHEMICAL_OXIDIZER),
    DISSOLVING("dissolving", MMLang.AUTHOR_DOLL, () -> AFBlockTypes.CHEMICAL_DISSOLUTION_CHAMBER, () -> MekanismBlocks.CHEMICAL_DISSOLUTION_CHAMBER);

    private final String registryNameComponent;
    private final ILangEntry langEntry;
    private final Supplier<MMMachine.MMFactoryMachine<?>> baseMachine;
    private final Supplier<BlockRegistryObject<?, ?>> baseBlock;

    AdvancedFactoryType(String registryNameComponent, ILangEntry langEntry, Supplier<MMMachine.MMFactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
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

    public MMMachine.MMFactoryMachine<?> getBaseMachine() {
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
