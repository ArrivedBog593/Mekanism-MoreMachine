package com.jerry.mekaf.common.upgrade;

import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import mekanism.common.upgrade.IUpgradeData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.Collections;
import java.util.List;

public class ItemToChemicalUpgradeData implements IUpgradeData {

    public final boolean redstone;
    public final IRedstoneControl.RedstoneControl controlType;
    public final IEnergyContainer energyContainer;
    public final int[] progress;
    public final boolean sorting;
    public final EnergyInventorySlot energySlot;
    public final List<IInventorySlot> inputSlots;
    public final List<IChemicalTank> outputTanks;
    public final CompoundTag components;


    public ItemToChemicalUpgradeData(HolderLookup.Provider provider, boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                     IEnergyContainer energyContainer, int operatingTicks, EnergyInventorySlot energySlot,
                                     InputInventorySlot inputSlot, IChemicalTank outputTank, List<ITileComponent> components) {
        this(provider, redstone, controlType, energyContainer, new int[]{operatingTicks}, energySlot, Collections.singletonList(inputSlot),
                Collections.singletonList(outputTank), false, components);
    }

    public ItemToChemicalUpgradeData(HolderLookup.Provider provider, boolean redstone, IRedstoneControl.RedstoneControl controlType,
                                     IEnergyContainer energyContainer, int[] progress, EnergyInventorySlot energySlot,
                                     List<IInventorySlot> inputSlots, List<IChemicalTank> outputTanks, boolean sorting, List<ITileComponent> components) {
        this.redstone = redstone;
        this.controlType = controlType;
        this.energyContainer = energyContainer;
        this.progress = progress;
        this.energySlot = energySlot;
        this.inputSlots = inputSlots;
        this.outputTanks = outputTanks;
        this.sorting = sorting;
        this.components = new CompoundTag();
        for (ITileComponent component : components) {
            component.write(this.components, provider);
        }
    }
}
