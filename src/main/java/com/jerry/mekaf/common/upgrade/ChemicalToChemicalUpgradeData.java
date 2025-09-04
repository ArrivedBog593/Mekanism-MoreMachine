package com.jerry.mekaf.common.upgrade;

import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.IUpgradeData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.Collections;
import java.util.List;

public class ChemicalToChemicalUpgradeData implements IUpgradeData {
    public final boolean redstone;
    public final IRedstoneControl.RedstoneControl controlType;
    public final IEnergyContainer energyContainer;
    public final int[] progress;
    public final boolean sorting;
    public final EnergyInventorySlot energySlot;
    public final List<IChemicalTank> inputTanks;
    public final List<IChemicalTank> outputTanks;
    public final CompoundTag components;


    public ChemicalToChemicalUpgradeData(HolderLookup.Provider provider, boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                     IEnergyContainer energyContainer, int operatingTicks, EnergyInventorySlot energySlot,
                                         IChemicalTank inputTank, IChemicalTank outputTank, List<ITileComponent> components) {
        this(provider, redstone, controlType, energyContainer, new int[]{operatingTicks}, energySlot, Collections.singletonList(inputTank),
                Collections.singletonList(outputTank), false, components);
    }

    public ChemicalToChemicalUpgradeData(HolderLookup.Provider provider, boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                     IEnergyContainer energyContainer, int[] progress, EnergyInventorySlot energySlot,
                                     List<IChemicalTank> inputTanks, List<IChemicalTank> outputTanks, boolean sorting, List<ITileComponent> components) {
        this.redstone = redstone;
        this.controlType = controlType;
        this.energyContainer = energyContainer;
        this.progress = progress;
        this.energySlot = energySlot;
        this.inputTanks = inputTanks;
        this.outputTanks = outputTanks;
        this.sorting = sorting;
        this.components = new CompoundTag();
        for (ITileComponent component : components) {
            component.write(this.components);
        }
    }
}
