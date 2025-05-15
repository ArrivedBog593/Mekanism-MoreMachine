package com.jerry.mekmm.common.upgrade;

import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl.RedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;
import net.minecraft.core.HolderLookup;

import java.util.Collections;
import java.util.List;

public class PlantingUpgradeData extends MachineUpgradeData {

    public final IChemicalTank stored;
    public final ChemicalInventorySlot chemicalSlot;
    public final long[] usedSoFar;

    //Planting Station Constructor
    public PlantingUpgradeData(HolderLookup.Provider provider, boolean redstone, RedstoneControl controlType, IEnergyContainer energyContainer, int operatingTicks, long usedSoFar, IChemicalTank stored,
                               EnergyInventorySlot energySlot, ChemicalInventorySlot chemicalSlot, InputInventorySlot inputSlot, OutputInventorySlot outputSlot, OutputInventorySlot secondaryOutputSlot,
                               List<ITileComponent> components) {
        this(provider, redstone, controlType, energyContainer, new int[]{operatingTicks}, new long[]{usedSoFar}, stored, energySlot, chemicalSlot,
                Collections.singletonList(inputSlot), List.of(outputSlot, secondaryOutputSlot), false, components);
    }

    //Planting Factory Constructor
    public PlantingUpgradeData(HolderLookup.Provider provider, boolean redstone, RedstoneControl controlType, IEnergyContainer energyContainer, int[] progress, long[] usedSoFar, IChemicalTank stored,
                               EnergyInventorySlot energySlot, ChemicalInventorySlot chemicalSlot, List<IInventorySlot> inputSlots, List<IInventorySlot> outputSlots, boolean sorting, List<ITileComponent> components) {
        super(provider, redstone, controlType, energyContainer, progress, energySlot, inputSlots, outputSlots, sorting, components);
        this.stored = stored;
        this.chemicalSlot = chemicalSlot;
        this.usedSoFar = usedSoFar;
    }
}