package com.jerry.mekmm.common.tile;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.tile.interfaces.ITileConnect;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.content.network.transmitter.LogisticalTransporterBase;
import mekanism.common.integration.energy.BlockEnergyCapabilityCache;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.lib.inventory.HandlerTransitRequest;
import mekanism.common.lib.inventory.TransitRequest.TransitResponse;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.*;
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
    public Map<BlockPos, Table<Direction, TransmissionType, BlockEnergyCapabilityCache>> energyCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IFluidHandler, @Nullable Direction>>> fluidCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> chemicalCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IItemHandler, @Nullable Direction>>> itemCapabilityCache;
    public Map<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IHeatHandler, @Nullable Direction>>> heatCapabilityCache;

    public static final long MAX_CHEMICAL = 10_000;
    public static final int MAX_FLUID = 10_000;
    public static final double HEAT_CAPACITY = 10;
    public static final double INVERSE_CONDUCTION_COEFFICIENT = 2;
    public static final double INVERSE_INSULATION_COEFFICIENT = 100;
    public static final double MAX_MULTIPLIER_TEMP = 10_000;

    private double lastTransferLoss;
    private double lastEnvironmentLoss;

    public BasicFluidTank fluidTank;
    public IChemicalTank chemicalTank;
    public BasicInventorySlot leftInventorySlot;
    public BasicInventorySlot rightInventorySlot;
    public MachineEnergyContainer<TileEntityWirelessTransmissionStation> energyContainer;
    public BasicHeatCapacitor heatCapacitor;

    private FluidInventorySlot fluidFillSlot;
    private FluidInventorySlot fluidDrainSlot;
    private OutputInventorySlot fluidOutputSlot;
    private ChemicalInventorySlot chemicalInputSlot;
    private ChemicalInventorySlot chemicalOutputSlot;
    private EnergyInventorySlot energySlot;

    public TileEntityWirelessTransmissionStation(BlockPos pos, BlockState state) {
        super(MoreMachineBlocks.WIRELESS_TRANSMISSION_STATION, pos, state);
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.FRONT);
        configComponent.setupIOConfig(TransmissionType.FLUID, fluidTank, RelativeSide.LEFT);
        configComponent.setupIOConfig(TransmissionType.CHEMICAL, chemicalTank, RelativeSide.RIGHT);
        configComponent.setupItemIOConfig(List.of(leftInventorySlot, rightInventorySlot, fluidFillSlot, chemicalInputSlot), List.of(fluidDrainSlot, chemicalOutputSlot, fluidOutputSlot), energySlot, false);
        configComponent.setupIOConfig(TransmissionType.HEAT, heatCapacitor, RelativeSide.BACK);
        configComponent.addDisabledSides(RelativeSide.TOP);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ENERGY, TransmissionType.HEAT);

        energyCapabilityCache = new ConcurrentHashMap<>();
        fluidCapabilityCache = new ConcurrentHashMap<>();
        chemicalCapabilityCache = new ConcurrentHashMap<>();
        itemCapabilityCache = new ConcurrentHashMap<>();
        heatCapabilityCache = new ConcurrentHashMap<>();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(leftInventorySlot = BasicInventorySlot.at(listener, 8, 77));
        builder.addSlot(rightInventorySlot = BasicInventorySlot.at(listener, 152, 77));
        builder.addSlot(chemicalInputSlot = ChemicalInventorySlot.fill(chemicalTank, listener, 28, 15));
        builder.addSlot(chemicalOutputSlot = ChemicalInventorySlot.drain(chemicalTank, listener, 28, 57));
        builder.addSlot(fluidFillSlot = FluidInventorySlot.fill(fluidTank, listener, 131, 15));
        builder.addSlot(fluidDrainSlot = FluidInventorySlot.drain(fluidTank, listener, 131, 57));
        builder.addSlot(fluidOutputSlot = OutputInventorySlot.at(listener, 131, 36));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 28, 36));
        chemicalInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        chemicalOutputSlot.setSlotOverlay(SlotOverlay.PLUS);
        fluidFillSlot.setSlotOverlay(SlotOverlay.MINUS);
        fluidDrainSlot.setSlotOverlay(SlotOverlay.PLUS);
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

    @Override
    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSideWithConfig(this);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(HEAT_CAPACITY, INVERSE_CONDUCTION_COEFFICIENT, INVERSE_INSULATION_COEFFICIENT, ambientTemperature, listener));
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        chemicalInputSlot.fillTank();
        chemicalOutputSlot.drainTank();
        fluidFillSlot.fillTank(fluidOutputSlot);
        fluidDrainSlot.drainTank(fluidOutputSlot);
        energySlot.fillContainerOrConvert();
        //TODO:添加一个延时，不需要每tick都发送（2秒发送一次应该可以）
        //传输能量
        if (energyCapabilityCache != null) CableUtils.emit(getEnergyCapabilityCache(), energyContainer, 1);
        //传输流体
        if (fluidCapabilityCache != null) FluidUtils.emit(getFluidCapabilityCache(), fluidTank, 1);
        //传输化学品
        if (chemicalCapabilityCache != null) ChemicalUtil.emit(getChemicalCapabilityCache(), chemicalTank, 2);
        //传输物品
        if (itemCapabilityCache != null) transportItems();
        //传输热量
        HeatTransfer loss = simulate();
        //如果有无线交换热量缓存时要加上无线交换的热量损失
        //交换热量需要每tick进行
        lastTransferLoss = loss.adjacentTransfer() + (heatCapabilityCache != null ? exchangeHeat() : 0);
        lastEnvironmentLoss = loss.environmentTransfer();
        return sendUpdatePacket;
    }

    private Collection<BlockEnergyCapabilityCache> getEnergyCapabilityCache() {
        //创建空list
        Collection<BlockEnergyCapabilityCache> energyCapCache = new ArrayList<>(Collections.emptyList());
        for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockEnergyCapabilityCache>> entry : energyCapabilityCache.entrySet()) {
            if (entry.getValue() != null) {
                for (Direction direction : Direction.values()) {
                    //无线传输站的对应配置不为空，就可以开始传输能量了
                    var cap = entry.getValue().get(direction, TransmissionType.ENERGY);
                    if (cap != null) {
                        energyCapCache.add(cap);
                    }
                }
            }
        }
        return energyCapCache;
    }

    private Collection<BlockCapabilityCache<IFluidHandler, @Nullable Direction>> getFluidCapabilityCache() {
        //创建空list
        Collection<BlockCapabilityCache<IFluidHandler, @Nullable Direction>> fluidCapCache = new ArrayList<>(Collections.emptyList());
        if (fluidCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IFluidHandler, @Nullable Direction>>> entry : fluidCapabilityCache.entrySet()) {
                if (entry.getValue() != null) {
                    for (Direction direction : Direction.values()) {
                        //无线传输站的对应配置不为空，就可以开始传输流体了
                        var cap = entry.getValue().get(direction, TransmissionType.FLUID);
                        if (cap != null) {
                            fluidCapCache.add(cap);
                        }
                    }
                }
            }
        }
        return fluidCapCache;
    }

    private Collection<BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> getChemicalCapabilityCache() {
        //创建空list
        Collection<BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> chemicalCapCache = new ArrayList<>(Collections.emptyList());
        if (chemicalCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> entry : chemicalCapabilityCache.entrySet()) {
                if (entry.getValue() != null) {
                    for (Direction direction : Direction.values()) {
                        //无线传输站的对应配置不为空，就可以开始传输化学品了
                        var cap = entry.getValue().get(direction, TransmissionType.CHEMICAL);
                        if (cap != null) {
                            chemicalCapCache.add(cap);
                        }
                    }
                }
            }
        }
        return chemicalCapCache;
    }

    private void transportItems() {
        //TODO:似乎还不能平分
        //获取自身的弹出能力
        IItemHandler selfEjectHandler = Capabilities.ITEM.createCache((ServerLevel) level, getBlockPos(), Direction.DOWN).getCapability();
        if (itemCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IItemHandler, @Nullable Direction>>> entry : itemCapabilityCache.entrySet()) {
                if (entry.getValue() != null) {
                    //循环已有的面
                    for (Direction direction : entry.getValue().rowKeySet()) {
                        //获取连接到的方块的能力缓存
                        var cap = entry.getValue().get(direction, TransmissionType.ITEM);
                        if (cap != null) {
                            //获取能力
                            IItemHandler targetHandler = cap.getCapability();
                            if (targetHandler != null) {
                                //另一种获取输出的方法
                                HandlerTransitRequest ejectMap = InventoryUtils.getEjectItemMap(selfEjectHandler, List.of(leftInventorySlot));
                                if (!ejectMap.isEmpty() && selfEjectHandler != null) {
                                    //TODO:为什么这里使用useAll会导致只有在输入且输出的情况下才传输，其他情况只要不是空就会一直刷物品
                                    TransitResponse response = ejectMap.eject(this, getBlockPos(), targetHandler, 0, LogisticalTransporterBase::getColor);
                                    if (!response.isEmpty()) {
                                        int sendingAmount = response.getSendingAmount();
                                        MekanismUtils.logMismatchedStackSize(leftInventorySlot.shrinkStack(sendingAmount, Action.EXECUTE), sendingAmount);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //与ITileHeatHandler中的simulateAdjacent()相似
    private double exchangeHeat() {
        double adjacentTransfer = 0;
        if (heatCapabilityCache != null) {
            for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IHeatHandler, @Nullable Direction>>> entry : heatCapabilityCache.entrySet()) {
                if (entry.getValue() != null) {
                    for (Direction direction : entry.getValue().rowKeySet()) {
                        var cap = entry.getValue().get(direction, TransmissionType.HEAT);
                        if (cap != null) {
                            //相当于getAdjacentUnchecked(Direction side)
                            adjacentHeatCaps.put(direction, cap);
                            //检查该方向是否有相邻的热处理系统
                            IHeatHandler sink = cap.getCapability();
                            //只有存在相邻系统时才进行热交换计算
                            if (sink != null) {
                                //获取当前系统该方向的热容量（在simulateAdjacent()中是这样，但在这只是获取热量容器的热容量）
                                double heatCapacity = heatCapacitor.getHeatCapacity();
                                //计算两个系统间的总热阻
                                //将getTotalInverseConductionCoefficient(direction)替换为heatCapacitor.getInverseConduction()
                                //因为远程传递热量将直接与热量容器交互
                                double invConduction = sink.getTotalInverseConduction() + heatCapacitor.getInverseConduction();
                                //计算温度传递量
                                //将getTotalTemperature(direction)替换为getTemperature()<=>heatCapacitor.getTemperature()
                                //将getAmbientTemperature(direction)删去，因为无线传输没有与环境温度的交互
                                double tempToTransfer = getTemperature() / invConduction;

                                //将温度差转换为实际热量Q = ΔT × C
                                double heatToTransfer = tempToTransfer * heatCapacity;
                                //当前系统失去热量
                                handleHeat(-heatToTransfer, direction);
                                //Note: Our sinks in mek are "lazy" but they will update the next tick if needed
                                sink.handleHeat(heatToTransfer);
                                adjacentTransfer = incrementAdjacentTransfer(adjacentTransfer, tempToTransfer, direction);
                            }
                        }
                    }
                }
            }
        }
        return adjacentTransfer;
    }

    @Override
    public ConnectStatus connectOrCut(BlockPos blockPos, Direction direction, TransmissionType type) {
        if (blockPos != null && direction != null && type != null) {
            switch (type) {
                case ENERGY -> {
                    //如果没有元素则创建一个新的table
                    Table<Direction, TransmissionType, BlockEnergyCapabilityCache> energyTables = energyCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    //如果table中存在已有行列，则删除该行列
                    if (energyTables.contains(direction, type)) {
                        energyTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        //如果点击的面没用对应的能力，则不进行连接
                        if (WorldUtils.getCapability(getLevel(), Capabilities.ENERGY.block(), blockPos, direction) != null) {
                            energyTables.put(direction, type, BlockEnergyCapabilityCache.create((ServerLevel) level, blockPos, direction));
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
                case HEAT -> {
                    Table<Direction, TransmissionType, BlockCapabilityCache<IHeatHandler, @Nullable Direction>> heatTables = heatCapabilityCache.computeIfAbsent(blockPos, pos -> HashBasedTable.create());
                    if (heatTables.contains(direction, type)) {
                        heatTables.remove(direction, type);
                        return ConnectStatus.DISCONNECT;
                    } else {
                        if (WorldUtils.getCapability(getLevel(), Capabilities.HEAT, blockPos, direction) != null) {
                            heatTables.put(direction, type, BlockCapabilityCache.create(Capabilities.HEAT, (ServerLevel) level, blockPos, direction));
                            heatCapabilityCache.put(blockPos, heatTables);
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

    public long getMaxEnergyOutput() {
        return 1;
    }

    public double getTemperature() {
        return heatCapacitor.getTemperature();
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbtTags, provider);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }

    public enum ConnectStatus {
        CONNECT,
        DISCONNECT,
        CONNECT_FAIL
    }
}
