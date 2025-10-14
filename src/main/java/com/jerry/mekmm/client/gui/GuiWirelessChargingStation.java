package com.jerry.mekmm.client.gui;

import com.jerry.mekmm.client.gui.element.GuiWirelessChargingStationSwitch;
import com.jerry.mekmm.client.gui.element.GuiWirelessChargingStationSwitch.SwitchType;
import com.jerry.mekmm.common.MoreMachineLang;
import com.jerry.mekmm.common.network.to_server.MoreMachinePacketGuiInteract;
import com.jerry.mekmm.common.network.to_server.MoreMachinePacketGuiInteract.MMGuiInteraction;
import com.jerry.mekmm.common.tile.TileEntityWirelessChargingStation;
import mekanism.client.SpecialColors;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiSideHolder;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.PacketUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.text.EnergyDisplay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiWirelessChargingStation extends GuiConfigurableTile<TileEntityWirelessChargingStation, MekanismTileContainer<TileEntityWirelessChargingStation>> {

    private static final ResourceLocation EQUIPS = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "switch/eject.png");
    private static final ResourceLocation INVENTORY = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "switch/input.png");
    private static final ResourceLocation CURIOS = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "switch/silk.png");

    public GuiWirelessChargingStation(MekanismTileContainer<TileEntityWirelessChargingStation> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addSecurityTab() {
        addRenderableWidget(new GuiSecurityTab(this, tile, 6));
    }

    @Override
    protected void addGuiElements() {
        addRenderableWidget(new GuiInnerScreen(this, imageWidth + 2, 40, 20, 77));
        addRenderableWidget(GuiSideHolder.create(this, imageWidth, 36, 85, false, true, SpecialColors.TAB_ARMOR_SLOTS));
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyGauge(tile.getEnergyContainer(), GaugeType.WIDE, this, 55, 18));
        addRenderableWidget(new GuiEnergyTab(this, () -> List.of(MekanismLang.MATRIX_INPUT_RATE.translate(EnergyDisplay.of(tile.getInputRate())),
                MekanismLang.MAX_OUTPUT.translate(EnergyDisplay.of(tile.getOutput())))));

        addRenderableWidget(new GuiWirelessChargingStationSwitch(this, imageWidth + 4, 42, EQUIPS, tile::getChargeEquipment, (element, mouseX, mouseY) ->
                PacketUtils.sendToServer(new MoreMachinePacketGuiInteract(MMGuiInteraction.CHARGING_EQUIPS, ((GuiWirelessChargingStation) element.gui()).tile)), SwitchType.LOWER_ICON))
                .setTooltip(MoreMachineLang.CHARGING_EQUIPS);
        addRenderableWidget(new GuiWirelessChargingStationSwitch(this, imageWidth + 4, 67, INVENTORY, tile::getChargeInventory, (element, mouseX, mouseY) ->
                PacketUtils.sendToServer(new MoreMachinePacketGuiInteract(MMGuiInteraction.CHARGING_INVENTORY, ((GuiWirelessChargingStation) element.gui()).tile)), SwitchType.LOWER_ICON))
                .setTooltip(MoreMachineLang.CHARGING_INVENTORY);
        addRenderableWidget(new GuiWirelessChargingStationSwitch(this, imageWidth + 4, 92, CURIOS, tile::getChargeCurios, (element, mouseX, mouseY) ->
                PacketUtils.sendToServer(new MoreMachinePacketGuiInteract(MMGuiInteraction.CHARGING_CURIOS, ((GuiWirelessChargingStation) element.gui()).tile)), SwitchType.LOWER_ICON))
                .setTooltip(MoreMachineLang.CHARGING_CURIOS);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
