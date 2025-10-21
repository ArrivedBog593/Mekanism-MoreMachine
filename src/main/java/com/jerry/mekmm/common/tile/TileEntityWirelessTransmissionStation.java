package com.jerry.mekmm.common.tile;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.tile.interfaces.ITileConnect;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.energy.BlockEnergyCapabilityCache;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.inventory.TransitRequest;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.FluidUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TileEntityWirelessTransmissionStation extends TileEntityConfigurableMachine implements ITileConnect {

    //TODO:或许有办法能把他们合在一起
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IStrictEnergyHandler, @Nullable Direction>>> energyCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IFluidHandler, @Nullable Direction>>> fluidCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> chemicalCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IItemHandler, @Nullable Direction>>> itemCapabilityCache;
    private Table<Direction, BlockPos, BlockCapabilityCache<IHeatHandler, @Nullable Direction>> heatCapabilityCaches = HashBasedTable.create();

    public static final long MAX_CHEMICAL = 10_000;
    public static final int MAX_FLUID = 10_000;

    public BasicFluidTank fluidTank;
    public IChemicalTank chemicalTank;
    public BasicInventorySlot inventorySlot;
    public MachineEnergyContainer<TileEntityWirelessTransmissionStation> energyContainer;
    public BasicHeatCapacitor heatCapacitor;

    public TileEntityWirelessTransmissionStation(BlockPos pos, BlockState state) {
        super(MoreMachineBlocks.WIRELESS_TRANSMISSION_STATION, pos, state);
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.FRONT);
        configComponent.setupIOConfig(TransmissionType.FLUID, fluidTank, RelativeSide.LEFT);
        configComponent.setupIOConfig(TransmissionType.CHEMICAL, chemicalTank, RelativeSide.RIGHT);
        configComponent.setupIOConfig(TransmissionType.ITEM, inventorySlot, RelativeSide.BOTTOM, true);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ENERGY);

        energyCapabilityCache = new ConcurrentHashMap<>();
        fluidCapabilityCache = new ConcurrentHashMap<>();
        chemicalCapabilityCache = new ConcurrentHashMap<>();
        itemCapabilityCache = new ConcurrentHashMap<>();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(inventorySlot = BasicInventorySlot.at(listener, 40, 10));
        return builder.build();
    }

    @Override
    public @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(chemicalTank = BasicChemicalTank.createModern(MAX_CHEMICAL, ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), listener));
        return builder.build();
    }

    @Override
    protected @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);
        builder.addTank(fluidTank = BasicFluidTank.create(MAX_FLUID, listener));
        return builder.build();
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

//    @Override
//    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
//        return super.getInitialHeatCapacitors(listener, ambientTemperature);
//    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        getEnergyCapabilityCache();
        FluidUtils.emit(getFluidCapabilityCache(), fluidTank, 1);
        ChemicalUtil.emit(getChemicalCapabilityCache(), chemicalTank, 1);
        getItemCapabilityCache();
        return sendUpdatePacket;
    }

    public Collection<BlockCapabilityCache<IStrictEnergyHandler, @Nullable Direction>> getEnergyCapabilityCache() {
        //创建空list
        Collection<BlockCapabilityCache<IStrictEnergyHandler, @Nullable Direction>> energyCapCache = new ArrayList<>(Collections.emptyList());
        if (energyCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IStrictEnergyHandler, @Nullable Direction>>> entry : energyCapabilityCache.entrySet()) {
                for (Direction direction : Direction.values()) {
                    if (entry.getValue() != null) {
                        //无线传输站的对应配置不为空，就可以开始传输能量了
                        var cap = entry.getValue().get(direction, TransmissionType.ENERGY);
                        if (cap != null){
                            energyCapCache.add(cap);
                            BlockEnergyCapabilityCache.create((ServerLevel) level, entry.getKey(), direction);
                        }
                    }
                }
            }
        }
        return energyCapCache;
    }

    public Collection<BlockCapabilityCache<IFluidHandler, @Nullable Direction>> getFluidCapabilityCache() {
        Collection<BlockCapabilityCache<IFluidHandler, @Nullable Direction>> fluidCapCache = new ArrayList<>(Collections.emptyList());
        if (fluidCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IFluidHandler, @Nullable Direction>>> entry : fluidCapabilityCache.entrySet()) {
                for (Direction direction : Direction.values()) {
                    if (entry.getValue() != null) {
                        var cap = entry.getValue().get(direction, TransmissionType.FLUID);
                        if (cap != null){
                            fluidCapCache.add(cap);
                        }
                    }
                }
            }
        }
        return fluidCapCache;
    }

    public Collection<BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> getChemicalCapabilityCache() {
        Collection<BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> chemicalCapCache = new ArrayList<>(Collections.emptyList());
        if (chemicalCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> entry : chemicalCapabilityCache.entrySet()) {
                for (Direction direction : Direction.values()) {
                    if (entry.getValue() != null) {
                        var cap = entry.getValue().get(direction, TransmissionType.CHEMICAL);
                        if (cap != null){
                            chemicalCapCache.add(cap);
                        }
                    }
                }
            }
        }
        return chemicalCapCache;
    }

    public void getItemCapabilityCache() {
//        Collection<BlockCapabilityCache<IItemHandler, @Nullable Direction>> itemCapCache = new ArrayList<>(Collections.emptyList());
        if (itemCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IItemHandler, @Nullable Direction>>> entry : itemCapabilityCache.entrySet()) {
                for (Direction direction : Direction.values()) {
                    if (entry.getValue() != null) {
                        var cap = entry.getValue().get(direction, TransmissionType.ITEM);
                        if (cap != null){
                            TransitRequest.anyItem(cap.getCapability(), 1);
//                            itemCapCache.add(cap);
                        }
                    }
                }
            }
        }
//        return itemCapCache;
    }

    @Override
    public ConnectStatus connectOrCut(BlockPos blockPos, Direction direction, TransmissionType type) {
        if (blockPos != null && direction != null && type != null) {
            switch (type) {
                case ENERGY -> {
                    //如果没有元素则创建一个新的table
                    Table<Direction, TransmissionType, BlockCapabilityCache<IStrictEnergyHandler, @Nullable Direction>> energyTables = energyCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    //如果table中存在已有行列，则删除该行列
                    if (energyTables.contains(direction, type)) {
                        energyTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        //如果点击的面没用对应的能力，则不进行连接
                        if (WorldUtils.getCapability(getLevel(), Capabilities.STRICT_ENERGY.block(), blockPos, direction) != null) {
                            energyTables.put(direction, type, Capabilities.STRICT_ENERGY.createCache((ServerLevel) level, blockPos, direction));
                            energyCapabilityCache.put(blockPos, energyTables);
                            return ConnectStatus.CONNECT;
                        }
                        return ConnectStatus.CONNECT_FAIL;
                    }
                }
                case FLUID -> {
                    Table<Direction, TransmissionType, BlockCapabilityCache<IFluidHandler, @Nullable Direction>> fluidTables = fluidCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    if (fluidTables.contains(direction, type)) {
                        fluidTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        if (WorldUtils.getCapability(getLevel(), Capabilities.FLUID.block(), blockPos, direction) != null) {
                            fluidTables.put(direction, type, Capabilities.FLUID.createCache((ServerLevel) level, blockPos, direction));
                            fluidCapabilityCache.put(blockPos, fluidTables);
                            return ConnectStatus.CONNECT;
                        }
                        return ConnectStatus.CONNECT_FAIL;
                    }
                }
                case CHEMICAL -> {
                    Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> chemicalTables = chemicalCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    if (chemicalTables.contains(direction, type)) {
                        chemicalTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        if (WorldUtils.getCapability(getLevel(), Capabilities.CHEMICAL.block(), blockPos, direction) != null) {
                            chemicalTables.put(direction, type, Capabilities.CHEMICAL.createCache((ServerLevel) level, blockPos, direction));
                            chemicalCapabilityCache.put(blockPos, chemicalTables);
                            return ConnectStatus.CONNECT;
                        }
                        return ConnectStatus.CONNECT_FAIL;
                    }
                }
                case ITEM -> {
                    Table<Direction, TransmissionType, BlockCapabilityCache<IItemHandler, @Nullable Direction>> itemTables = itemCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    if (itemTables.contains(direction, type)) {
                        itemTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        if (WorldUtils.getCapability(getLevel(), Capabilities.ITEM.block(), blockPos, direction) != null) {
                            itemTables.put(direction, type, Capabilities.ITEM.createCache((ServerLevel) level, blockPos, direction));
                            itemCapabilityCache.put(blockPos, itemTables);
                            return ConnectStatus.CONNECT;
                        }
                        return ConnectStatus.CONNECT_FAIL;
                    }
                }
            }
        }
        return ConnectStatus.CONNECT_FAIL;
    }

    public @Nullable MachineEnergyContainer<TileEntityWirelessTransmissionStation> getEnergyContainer() {
        return energyContainer;
    }

    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> getCap() {
        return chemicalCapabilityCache;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbtTags, provider);
    }

    public enum ConnectStatus {
        CONNECT,
        DISCONNECT,
        CONNECT_FAIL
    }
}
