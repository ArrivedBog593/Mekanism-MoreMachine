package com.jerry.meklm.common.capabilities.holder.fluid;

import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.common.capabilities.holder.BasicHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CanAdjustFluidTankHolder extends BasicHolder<IExtendedFluidTank> implements IFluidTankHolder {

    @Nullable
    private final Predicate<RelativeSide> insertPredicate;
    @Nullable
    private final Predicate<RelativeSide> extractPredicate;

    public CanAdjustFluidTankHolder(Supplier<Direction> facingSupplier, @Nullable Predicate<RelativeSide> insertPredicate, @Nullable Predicate<RelativeSide> extractPredicate) {
        super(facingSupplier);
        this.insertPredicate = insertPredicate;
        this.extractPredicate = extractPredicate;
    }

    void addTank(@NotNull IExtendedFluidTank tank, RelativeSide... sides) {
        addSlotInternal(tank, sides);
    }

    @Override
    public @NotNull List<IExtendedFluidTank> getTanks(@Nullable Direction direction) {
        return getSlots(direction);
    }

    @Override
    public boolean canInsert(@Nullable Direction direction) {
        //If the insert predicate is null then we can insert from any side, don't bother looking up our facing
        return direction != null && (insertPredicate == null || insertPredicate.test(RelativeSide.fromDirections(facingSupplier.get(), direction)));
    }

    @Override
    public boolean canExtract(@Nullable Direction direction) {
        //If the extract predicate is null then we can extract from any side, don't bother looking up our facing
        return direction != null && (extractPredicate == null || extractPredicate.test(RelativeSide.fromDirections(facingSupplier.get(), direction)));
    }
}
