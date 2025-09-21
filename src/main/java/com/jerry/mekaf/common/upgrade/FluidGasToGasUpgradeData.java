package com.jerry.mekaf.common.upgrade;

import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;

import java.util.Collections;
import java.util.List;

public class FluidGasToGasUpgradeData extends GasToGasUpgradeData {

    public final FluidInventorySlot fluidInputSlot;
    public final OutputInventorySlot fluidOutputSlot;
    public final BasicFluidTank inputTank;
    public final long[] usedSoFar;

    public FluidGasToGasUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                    IEnergyContainer energyContainer, int operatingTicks, long usedSoFar, EnergyInventorySlot energySlot,
                                    FluidInventorySlot fluidInputSlot, OutputInventorySlot fluidOutputSlot, IGasTank inputSlot,
                                    BasicFluidTank inputTank, IGasTank outputTank, List<ITileComponent> components) {
        this(redstone, controlType, energyContainer, new int[]{operatingTicks}, new long[]{usedSoFar}, energySlot, fluidInputSlot, fluidOutputSlot,
                Collections.singletonList(inputSlot), inputTank, Collections.singletonList(outputTank), false, components);
    }

    public FluidGasToGasUpgradeData(boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                    IEnergyContainer energyContainer, int[] progress, long[] usedSoFar, EnergyInventorySlot energySlot,
                                    FluidInventorySlot fluidInputSlot, OutputInventorySlot fluidOutputSlot, List<IGasTank> inputSlots,
                                    BasicFluidTank inputTank, List<IGasTank> outputTanks, boolean sorting, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, progress, energySlot, inputSlots, outputTanks, sorting, components);
        this.fluidInputSlot = fluidInputSlot;
        this.fluidOutputSlot = fluidOutputSlot;
        this.inputTank = inputTank;
        this.usedSoFar = usedSoFar;
    }
}
