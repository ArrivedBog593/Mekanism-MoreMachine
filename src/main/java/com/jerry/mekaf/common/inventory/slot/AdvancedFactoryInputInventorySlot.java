package com.jerry.mekaf.common.inventory.slot;

import com.jerry.mekaf.common.tile.factory.TileEntityItemToChemicalFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityLiquifyingFactory;
import com.jerry.mekaf.common.tile.factory.TileEntityPressurizedReactingFactory;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AdvancedFactoryInputInventorySlot extends InputInventorySlot {

    public static AdvancedFactoryInputInventorySlot create(TileEntityItemToChemicalFactory<?> factory, int process, IChemicalTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputTank, "Chemical output tank cannot be null");
        return new AdvancedFactoryInputInventorySlot(factory, process, outputTank, listener, x, y);
    }

    private AdvancedFactoryInputInventorySlot(TileEntityItemToChemicalFactory<?> factory, int process, IChemicalTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.isItemValidForSlot(stack) && factory.inputProducesOutput(process, stack, outputTank, false),
                factory::isValidInputItem, listener, x, y);
    }

    public static AdvancedFactoryInputInventorySlot create(TileEntityPressurizedReactingFactory factory, int process, IInventorySlot outputSlot, IChemicalTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputTank, "Chemical output tank cannot be null");
        return new AdvancedFactoryInputInventorySlot(factory, process, outputSlot, outputTank, listener, x, y);
    }

    private AdvancedFactoryInputInventorySlot(TileEntityPressurizedReactingFactory factory, int process, IInventorySlot outputSlot, IChemicalTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.isItemValidForSlot(stack) && factory.inputProducesOutput(process, stack, outputSlot, outputTank, false),
                factory::isValidInputItem, listener, x, y);
    }

    public static AdvancedFactoryInputInventorySlot create(TileEntityLiquifyingFactory factory, int process, IInventorySlot outputSlot, IExtendedFluidTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputTank, "Fluid output tank cannot be null");
        return new AdvancedFactoryInputInventorySlot(factory, process, outputSlot, outputTank, listener, x, y);
    }

    private AdvancedFactoryInputInventorySlot(TileEntityLiquifyingFactory factory, int process, IInventorySlot outputSlot, IExtendedFluidTank outputTank, @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.isItemValidForSlot(stack) && factory.inputProducesOutput(process, stack, outputSlot, outputTank, false),
                factory::isValidInputItem, listener, x, y);
    }
}