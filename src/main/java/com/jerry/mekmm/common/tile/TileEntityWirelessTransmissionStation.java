package com.jerry.mekmm.common.tile;

import com.jerry.mekmm.api.MoreMachineSerializationConstants;
import com.jerry.mekmm.common.attachments.component.ConnectionConfig;
import com.jerry.mekmm.common.attachments.component.WirelessConnectionManager;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.inventory.container.tile.WirelessTransmissionStationConfigContainer;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.registries.MoreMachineDataComponents;
import com.jerry.mekmm.common.tile.interfaces.ITileConnectHolder;
import com.jerry.mekmm.common.tile.prefab.TileEntityConnectableMachine;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.heat.ITileHeatHandler;
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
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.lib.inventory.HandlerTransitRequest;
import mekanism.common.lib.inventory.TransitRequest.TransitResponse;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class TileEntityWirelessTransmissionStation extends TileEntityConnectableMachine implements ITileConnectHolder {

    public static final long DEFAULT_ENERGY_RATE = 0;
    public static final int DEFAULT_FLUIDS_RATE = 0;
    public static final long DEFAULT_CHEMICALS_RATE = 0;
    public static final int DEFAULT_ITEMS_RATE = 0;

    public final WirelessConnectionManager connectionManager = new WirelessConnectionManager(this);

    private long energyRate;
    private int fluidsRate;
    private long chemicalsRate;
    private int itemsRate;

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
    public BasicInventorySlot inventorySlot;
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
        energyRate = DEFAULT_ENERGY_RATE;
        fluidsRate = DEFAULT_FLUIDS_RATE;
        chemicalsRate = DEFAULT_CHEMICALS_RATE;
        itemsRate = DEFAULT_ITEMS_RATE;
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.FRONT);
        configComponent.setupIOConfig(TransmissionType.FLUID, fluidTank, RelativeSide.LEFT);
        configComponent.setupIOConfig(TransmissionType.CHEMICAL, chemicalTank, RelativeSide.RIGHT);
        configComponent.setupItemIOConfig(List.of(inventorySlot, fluidFillSlot, chemicalInputSlot), List.of(fluidDrainSlot, chemicalOutputSlot, fluidOutputSlot), energySlot, false);
        configComponent.setupIOConfig(TransmissionType.HEAT, heatCapacitor, RelativeSide.BACK);
        configComponent.addDisabledSides(RelativeSide.TOP);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ENERGY, TransmissionType.HEAT);
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(inventorySlot = BasicInventorySlot.at(listener, 8, 77));
//        builder.addSlot(rightInventorySlot = BasicInventorySlot.at(listener, 152, 77));
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

    private void closeInvalidScreens() {
        if (getActive() && !playersUsing.isEmpty()) {
            for (Player player : new HashSet<>(playersUsing)) {
                if (player.containerMenu instanceof WirelessTransmissionStationConfigContainer) {
                    player.closeContainer();
                }
            }
        }
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        closeInvalidScreens();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        closeInvalidScreens();
        chemicalInputSlot.fillTank();
        chemicalOutputSlot.drainTank();
        fluidFillSlot.fillTank(fluidOutputSlot);
        fluidDrainSlot.drainTank(fluidOutputSlot);
        energySlot.fillContainerOrConvert();
        //
        if (level != null && level.getGameTime() % 100 == 0) {
            connectionManager.validateConnections();
        }
        //TODO:添加一个延时，不需要每tick都发送（2秒发送一次应该可以）
        //传输能量
        CableUtils.emit(connectionManager.getEnergyCaches(), energyContainer, getEnergyRate());
        //传输流体
        FluidUtils.emit(connectionManager.getFluidCaches(), fluidTank, getFluidsRate());
        //传输化学品
        ChemicalUtil.emit(connectionManager.getChemicalCaches(), chemicalTank, getChemicalsRate());
        //传输物品
        transportItems();
        //传输热量
        HeatTransfer loss = simulate();
        //如果有无线交换热量缓存时要加上无线交换的热量损失
        //交换热量需要每tick进行
        //如果没有无线热量传递，则只计算相邻方块的热传导；如果有无线热量传递，则计算两者的加和
        lastTransferLoss = loss.adjacentTransfer();
        lastEnvironmentLoss = loss.environmentTransfer();
        return sendUpdatePacket;
    }

    private void transportItems() {
        //TODO:似乎还不能平分
        //获取自身的弹出能力
        IItemHandler selfHandler = Capabilities.ITEM.createCache((ServerLevel) level, getBlockPos(), Direction.DOWN).getCapability();
        if (selfHandler == null) return;

        for (BlockCapabilityCache<IItemHandler, Direction> cache : connectionManager.getItemCaches()) {
            IItemHandler target = cache.getCapability();
            if (target != null) {
                //另一种获取输出的方法
                HandlerTransitRequest request = InventoryUtils.getEjectItemMap(selfHandler, List.of(inventorySlot));
                if (!request.isEmpty()) {
                    //TODO:为什么这里使用useAll会导致只有在输入且输出的情况下才传输，其他情况只要不是空就会一直刷物品
                    TransitResponse response = request.eject(this, getBlockPos(), target, 0, LogisticalTransporterBase::getColor);
                    if (!response.isEmpty()) {
                        int amount = response.getSendingAmount();
//                        int amount = getItemsRate();
                        MekanismUtils.logMismatchedStackSize(inventorySlot.shrinkStack(amount, Action.EXECUTE), amount);
                    }
                }
            }
        }
    }

    /**
     * 与{@link ITileHeatHandler#simulateAdjacent()}相似
     *
     * @return double 与连接方块的热量传递值
     */
    private double exchangeHeat() {
        double adjacentTransfer = 0;
        for (ConnectionConfig config : connectionManager.getConnectionsByType(TransmissionType.HEAT)) {
            //检查该方向是否有相邻的热处理系统
            IHeatHandler sink = WorldUtils.getCapability(level, Capabilities.HEAT, config.pos(), config.direction());
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
                double tempToTransfer = getTemperature() - HeatAPI.AMBIENT_TEMP / invConduction;
                //将温度差转换为实际热量Q = ΔT × C
                double heatToTransfer = tempToTransfer * heatCapacity;
                //当前系统失去热量
                heatCapacitor.handleHeat(-heatToTransfer);
                //Note: Our sinks in mek are "lazy" but they will update the next tick if needed
                sink.handleHeat(heatToTransfer);
                adjacentTransfer = incrementAdjacentTransfer(adjacentTransfer, tempToTransfer, config.direction());
            }
        }
        return adjacentTransfer;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        connectionManager.clear();
    }

    @Override
    public double simulateAdjacent() {
        return super.simulateAdjacent() + exchangeHeat();
    }

    @Override
    public ConnectStatus connectOrCut(BlockPos blockPos, Direction direction, TransmissionType type) {
        ConnectStatus status = connectionManager.linkOrCut(blockPos, direction, type);
        if (status != ConnectStatus.CONNECT_FAIL && !isRemote()) {
            sendUpdatePacket();
            markForSave();
        }
        return status;
    }

    public @Nullable MachineEnergyContainer<TileEntityWirelessTransmissionStation> getEnergyContainer() {
        return energyContainer;
    }

    public long getEnergyRate() {
        return energyRate;
    }

    public int getFluidsRate() {
        return fluidsRate;
    }

    public long getChemicalsRate() {
        return chemicalsRate;
    }

    public int getItemsRate() {
        return itemsRate;
    }

    public void setEnergyRateFromPacket(long newRate) {
        setEnergyRate(Mth.clamp(newRate, 0, MoreMachineConfig.general.energyRate.get()));
    }

    public void setEnergyRate(long newRate) {
        if (energyRate != newRate) {
            energyRate = newRate;
        }
    }

    public void setFluidsRateFromPacket(int newRate) {
        setFluidsRate(Mth.clamp(newRate, 0, MoreMachineConfig.general.fluidsRate.get()));
    }

    public void setFluidsRate(int newRate) {
        if (fluidsRate != newRate) {
            fluidsRate = newRate;
        }
    }

    public void setChemicalsRateFromPacket(long newRate) {
        setChemicalsRate(Mth.clamp(newRate, 0, MoreMachineConfig.general.chemicalsRate.get()));
    }

    public void setChemicalsRate(long newRate) {
        if (chemicalsRate != newRate) {
            chemicalsRate = newRate;
        }
    }

    public void setItemsRateFromPacket(int newRate) {
        setItemsRate(Mth.clamp(newRate, 0, MoreMachineConfig.general.itemsRate.get()));
    }

    public void setItemsRate(int newRate) {
        if (itemsRate != newRate) {
            itemsRate = newRate;
        }
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
    public @NotNull CompoundTag getReducedUpdateTag(HolderLookup.@NotNull Provider provider) {
        CompoundTag updateTag = super.getReducedUpdateTag(provider);
        connectionManager.saveToNBT(updateTag);
        updateTag.putLong(MoreMachineSerializationConstants.ENERGY_RATE, getEnergyRate());
        updateTag.putInt(MoreMachineSerializationConstants.FLUIDS_RATE, getFluidsRate());
        updateTag.putLong(MoreMachineSerializationConstants.CHEMICALS_RATE, getChemicalsRate());
        updateTag.putInt(MoreMachineSerializationConstants.ITEM_RATE, getItemsRate());
        return updateTag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.handleUpdateTag(tag, provider);
        connectionManager.loadFromNBT(tag);
        NBTUtils.setIntIfPresent(tag, MoreMachineSerializationConstants.ENERGY_RATE, this::setEnergyRate);
        NBTUtils.setIntIfPresent(tag, MoreMachineSerializationConstants.FLUIDS_RATE, this::setFluidsRate);
        NBTUtils.setIntIfPresent(tag, MoreMachineSerializationConstants.CHEMICALS_RATE, this::setChemicalsRate);
        NBTUtils.setIntIfPresent(tag, MoreMachineSerializationConstants.ITEM_RATE, this::setItemsRate);
    }

    @Override
    public void readSustainedData(HolderLookup.Provider provider, CompoundTag dataMap) {
        super.readSustainedData(provider, dataMap);
        setEnergyRate(Math.min(dataMap.getInt(MoreMachineSerializationConstants.ENERGY_RATE), MoreMachineConfig.general.energyRate.get()));
        setFluidsRate(Math.min(dataMap.getInt(MoreMachineSerializationConstants.FLUIDS_RATE), MoreMachineConfig.general.fluidsRate.get()));
        setChemicalsRate(Math.min(dataMap.getInt(MoreMachineSerializationConstants.CHEMICALS_RATE), MoreMachineConfig.general.chemicalsRate.get()));
        setItemsRate(Math.min(dataMap.getInt(MoreMachineSerializationConstants.ITEM_RATE), MoreMachineConfig.general.itemsRate.get()));
    }

    @Override
    public void writeSustainedData(HolderLookup.Provider provider, CompoundTag dataMap) {
        super.writeSustainedData(provider, dataMap);
        dataMap.putLong(MoreMachineSerializationConstants.ENERGY_RATE, getEnergyRate());
        dataMap.putInt(MoreMachineSerializationConstants.FLUIDS_RATE, getFluidsRate());
        dataMap.putLong(MoreMachineSerializationConstants.CHEMICALS_RATE, getChemicalsRate());
        dataMap.putInt(MoreMachineSerializationConstants.ITEM_RATE, getItemsRate());
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        connectionManager.loadFromNBT(nbt);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbtTags, provider);
        connectionManager.saveToNBT(nbtTags);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        addConfigContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
        container.track(SyncableInt.create(connectionManager::getConnectionCount, count -> {
        }));
    }

    public void addConfigContainerTrackers(MekanismContainer container) {
        container.track(SyncableLong.create(this::getEnergyRate, this::setEnergyRate));
        container.track(SyncableInt.create(this::getFluidsRate, this::setFluidsRate));
        container.track(SyncableLong.create(this::getChemicalsRate, this::setChemicalsRate));
        container.track(SyncableInt.create(this::getItemsRate, this::setItemsRate));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(MoreMachineDataComponents.ENERGY_RATE, getEnergyRate());
        builder.set(MoreMachineDataComponents.FLUIDS_RATE, getFluidsRate());
        builder.set(MoreMachineDataComponents.CHEMICALS_RATE, getChemicalsRate());
        builder.set(MoreMachineDataComponents.ITEMS_RATE, getItemsRate());
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        setEnergyRate(Math.min(input.getOrDefault(MoreMachineDataComponents.ENERGY_RATE, energyRate), MoreMachineConfig.general.energyRate.get()));
        setFluidsRate(Math.min(input.getOrDefault(MoreMachineDataComponents.FLUIDS_RATE, fluidsRate), MoreMachineConfig.general.fluidsRate.get()));
        setChemicalsRate(Math.min(input.getOrDefault(MoreMachineDataComponents.CHEMICALS_RATE, chemicalsRate), MoreMachineConfig.general.chemicalsRate.get()));
        setItemsRate(Math.min(input.getOrDefault(MoreMachineDataComponents.ITEMS_RATE, itemsRate), MoreMachineConfig.general.itemsRate.get()));
    }

    @Override
    public WirelessConnectionManager getConnectManager() {
        return connectionManager;
    }
}
