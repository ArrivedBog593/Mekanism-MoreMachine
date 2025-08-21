package com.jerry.meklm.common.capabilities.holder.fluid;

import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.common.capabilities.holder.ConfigHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CanAdjustConfigFluidTankHolder extends ConfigHolder<IExtendedFluidTank> implements IFluidTankHolder {

    public CanAdjustConfigFluidTankHolder(ISideConfiguration sideConfiguration) {
        super(sideConfiguration);
    }

    void addTank(@NotNull IExtendedFluidTank tank) {
        slots.add(tank);
    }

    @Override
    protected TransmissionType getTransmissionType() {
        return TransmissionType.FLUID;
    }

    @NotNull
    @Override
    public List<IExtendedFluidTank> getTanks(@Nullable Direction direction) {
        return getSlots(direction, slotInfo -> slotInfo instanceof FluidSlotInfo info ? info.getTanks() : Collections.emptyList());
    }
}