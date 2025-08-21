package com.jerry.meklm.common.capabilities.holder.chemical;

import mekanism.api.AutomationType;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.lib.radiation.RadiationManager;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CanAdjustChemicalTankHelper {

    private final IChemicalTankHolder slotHolder;
    private boolean built;

    private CanAdjustChemicalTankHelper(IChemicalTankHolder slotHolder) {
        this.slotHolder = slotHolder;
    }

    public static BiPredicate<ChemicalStack, @NotNull AutomationType> radioactiveInputTankPredicate(Supplier<IChemicalTank> outputTank) {
        //Allow extracting out of the input gas tank if it isn't external OR the output tank is empty AND the input is radioactive
        //Note: This only is the case if radiation is enabled as otherwise things like gauge droppers can work as the way to remove radioactive contents
        return (type, automationType) -> automationType != AutomationType.EXTERNAL ||
                                         (outputTank.get().isEmpty() && type.isRadioactive() && RadiationManager.isGlobalRadiationEnabled());
    }

    public static CanAdjustChemicalTankHelper forSide(Supplier<Direction> facingSupplier) {
        return forSide(facingSupplier, null, null);
    }

    public static CanAdjustChemicalTankHelper forSide(Supplier<Direction> facingSupplier, @Nullable Predicate<RelativeSide> insertPredicate,
                                                   @Nullable Predicate<RelativeSide> extractPredicate) {
        return new CanAdjustChemicalTankHelper(new CanAdjustChemicalTankHolder(facingSupplier, insertPredicate, extractPredicate));
    }

    public static CanAdjustChemicalTankHelper forSideWithConfig(ISideConfiguration sideConfiguration) {
        return new CanAdjustChemicalTankHelper(new CanAdjustConfigChemicalTankHolder(sideConfiguration));
    }

    public IChemicalTank addTank(IChemicalTank tank) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof CanAdjustChemicalTankHolder tankHolder) {
            tankHolder.addTank(tank);
        } else if (slotHolder instanceof CanAdjustConfigChemicalTankHolder tankHolder) {
            tankHolder.addTank(tank);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add tanks");
        }
        return tank;
    }

    public IChemicalTank addTank(IChemicalTank tank, RelativeSide... sides) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof CanAdjustChemicalTankHolder tankHolder) {
            tankHolder.addTank(tank, sides);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add tanks on specific sides");
        }
        return tank;
    }

    public IChemicalTankHolder build() {
        built = true;
        return slotHolder;
    }
}