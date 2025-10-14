package com.jerry.mekmm.common.tile;

import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.curios.CuriosIntegration;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.StorageUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TileEntityWirelessChargingStation extends TileEntityConfigurableMachine implements IBoundingBlock {

    private MachineEnergyContainer<TileEntityWirelessChargingStation> energyContainer;

    private boolean chargeEquipment = false;
    private boolean chargeInventory = false;
    private boolean chargeCurios = false;

    EnergyInventorySlot chargeSlot;
    EnergyInventorySlot dischargeSlot;

    public TileEntityWirelessChargingStation(BlockPos pos, BlockState state) {
        super(MoreMachineBlocks.WIRELESS_CHARGING_STATION, pos, state);
        configComponent.setupIOConfig(TransmissionType.ITEM, chargeSlot, dischargeSlot, RelativeSide.FRONT, true);
        configComponent.setupIOConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.FRONT);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.ENERGY).setCanEject(type -> canFunction());
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(dischargeSlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 17, 35));
        builder.addSlot(chargeSlot = EnergyInventorySlot.drain(energyContainer, listener, 143, 35));
        dischargeSlot.setSlotOverlay(SlotOverlay.MINUS);
        chargeSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        chargeSlot.drainContainer();
        dischargeSlot.fillContainerOrConvert();
        if (!energyContainer.isEmpty() && isRedstoneActivated()) {
            Level level = getLevel();
            UUID uuid = getSecurity().getOwnerUUID();
            if (level != null && uuid != null) {
                Player player = level.getPlayerByUUID(uuid);
                if (player != null) {
                    //优先充能盔甲，其次是主副手和饰品，最后是物品栏
                    if (chargeEquipment) chargeSuit(player);
                    if (chargeInventory) chargeInventory(player);
                    if (chargeCurios) chargeCurios(player);
                }
            }
        }
        return sendUpdatePacket;
    }

    private void chargeSuit(Player player) {
        long toCharge = Math.min(MoreMachineConfig.general.wirelessChargingStationChargingRate.get(), energyContainer.getEnergy());
        if (toCharge == 0L) {
            return;
        }

        for (ItemStack stack : player.getArmorSlots()) {
            IEnergyContainer suitContainer = StorageUtils.getEnergyContainer(stack, 0);
            if (suitContainer != null) {
                toCharge = charge(energyContainer, stack, toCharge);
                if (toCharge == 0L) {
                    return;
                }
            }
        }
    }

    private void chargeInventory(Player player) {
        long toCharge = Math.min(MoreMachineConfig.general.wirelessChargingStationChargingRate.get(), energyContainer.getEnergy());
        if (toCharge == 0L) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        toCharge = charge(energyContainer, mainHand, toCharge);
        toCharge = charge(energyContainer, offHand, toCharge);
        if (toCharge > 0L) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack != mainHand && stack != offHand) {
                    toCharge = charge(energyContainer, stack, toCharge);
                    if (toCharge == 0L) {
                        return;
                    }
                }
            }

        }
    }

    private void chargeCurios(Player player) {
        long toCharge = Math.min(MoreMachineConfig.general.wirelessChargingStationChargingRate.get(), energyContainer.getEnergy());
        if (toCharge == 0L) {
            return;
        }

        if (Mekanism.hooks.curios.isLoaded()) {
            IItemHandler handler = CuriosIntegration.getCuriosInventory(player);
            if (handler != null) {
                for (int slot = 0, slots = handler.getSlots(); slot < slots; slot++) {
                    toCharge = charge(energyContainer, handler.getStackInSlot(slot), toCharge);
                    if (toCharge == 0L) {
                        return;
                    }
                }
            }
        }
    }

    private long charge(IEnergyContainer energyContainer, ItemStack stack, long amount) {
        if (!stack.isEmpty() && amount > 0L) {
            IStrictEnergyHandler handler = EnergyCompatUtils.getStrictEnergyHandler(stack);
            if (handler != null) {
                long remaining = handler.insertEnergy(amount, Action.SIMULATE);
                if (remaining < amount) {
                    long toExtract = amount - remaining;
                    long extracted = energyContainer.extract(toExtract, Action.EXECUTE, AutomationType.MANUAL);
                    long inserted = handler.insertEnergy(extracted, Action.EXECUTE);
                    return inserted + remaining;
                }
            }
        }
        return amount;
    }

    public void toggleChargeEquipment() {
        chargeEquipment = !chargeEquipment;
        markForSave();
    }

    public void toggleChargeInventory() {
        chargeInventory = !chargeInventory;
        markForSave();
    }

    public void toggleChargeCurios() {
        chargeCurios = !chargeCurios;
        markForSave();
    }

    public boolean getChargeEquipment() {
        return chargeEquipment;
    }

    public boolean getChargeInventory() {
        return chargeInventory;
    }

    public boolean getChargeCurios() {
        return chargeCurios;
    }

    public MachineEnergyContainer<TileEntityWirelessChargingStation> getEnergyContainer() {
        return energyContainer;
    }

    public long getOutput() {
        return Math.min(MekanismConfig.gear.mekaSuitInventoryChargeRate.get(), energyContainer.getEnergy());
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(this::getChargeEquipment, value -> chargeEquipment = value));
        container.track(SyncableBoolean.create(this::getChargeInventory, value -> chargeInventory = value));
        container.track(SyncableBoolean.create(this::getChargeCurios, value -> chargeCurios = value));
    }
}
