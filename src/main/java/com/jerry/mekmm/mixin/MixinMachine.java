package com.jerry.mekmm.mixin;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekaf.common.registries.AFBlocks;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = Machine.class, remap = false)
public abstract class MixinMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {

    public MixinMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(tileEntityRegistrar, description);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectFactoryUltimateToAbsolute(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description, CallbackInfo ci) {
        TileEntityTypeRegistryObject<?> tileEntityTypeRegistryObject = tileEntityRegistrar.get();
//        switch (tileEntityTypeRegistryObject) {
//            case MekanismTileEntityTypes.CHEMICAL_OXIDIZER
//        }
//        if (tileEntityTypeRegistryObject == MekanismTileEntityTypes.CHEMICAL_OXIDIZER) {
//            add(new AttributeUpgradeable(() -> AFBlocks.getAdvancedFactory(FactoryTier.BASIC, AdvancedFactoryType.OXIDIZING)));
//        }
    }
}
