package com.jerry.meklm.common.capabilities.holder.chemical;

import mekanism.api.chemical.IChemicalTank;
import mekanism.common.capabilities.holder.ConfigHolder;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.interfaces.ISideConfiguration;

import net.minecraft.core.Direction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CanAdjustConfigChemicalTankHolder extends ConfigHolder<IChemicalTank> implements IChemicalTankHolder {

    protected CanAdjustConfigChemicalTankHolder(ISideConfiguration sideConfiguration) {
        super(sideConfiguration);
    }

    void addTank(IChemicalTank tank) {
        slots.add(tank);
    }

    @NotNull
    @Override
    public List<IChemicalTank> getTanks(@Nullable Direction direction) {
        return getSlots(direction, slotInfo -> slotInfo instanceof ChemicalSlotInfo info ? info.getTanks() : Collections.emptyList());
    }

    @Override
    protected TransmissionType getTransmissionType() {
        return TransmissionType.CHEMICAL;
    }
}
