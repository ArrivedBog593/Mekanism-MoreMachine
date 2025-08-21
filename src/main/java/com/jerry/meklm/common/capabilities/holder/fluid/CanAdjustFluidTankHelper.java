package com.jerry.meklm.common.capabilities.holder.fluid;

import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class CanAdjustFluidTankHelper {

    private final IFluidTankHolder slotHolder;
    private boolean built;

    private CanAdjustFluidTankHelper(IFluidTankHolder slotHolder) {
        this.slotHolder = slotHolder;
    }

    public static CanAdjustFluidTankHelper forSide(Supplier<Direction> facingSupplier) {
        return forSide(facingSupplier, null, null);
    }

    public static CanAdjustFluidTankHelper forSide(Supplier<Direction> facingSupplier, @Nullable Predicate<RelativeSide> insertPredicate,
                                                   @Nullable Predicate<RelativeSide> extractPredicate) {
        return new CanAdjustFluidTankHelper(new CanAdjustFluidTankHolder(facingSupplier, insertPredicate, extractPredicate));
    }

    public static CanAdjustFluidTankHelper forSideWithConfig(ISideConfiguration sideConfiguration) {
        return new CanAdjustFluidTankHelper(new CanAdjustConfigFluidTankHolder(sideConfiguration));
    }

    public <TANK extends IExtendedFluidTank> TANK addTank(@NotNull TANK tank) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof CanAdjustFluidTankHolder slotHolder) {
            slotHolder.addTank(tank);
        } else if (slotHolder instanceof CanAdjustConfigFluidTankHolder slotHolder) {
            slotHolder.addTank(tank);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add tanks");
        }
        return tank;
    }

    public <TANK extends IExtendedFluidTank> TANK addTank(@NotNull TANK tank, RelativeSide... sides) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof CanAdjustFluidTankHolder slotHolder) {
            slotHolder.addTank(tank, sides);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add tanks on specific sides");
        }
        return tank;
    }

    public IFluidTankHolder build() {
        built = true;
        return slotHolder;
    }
}