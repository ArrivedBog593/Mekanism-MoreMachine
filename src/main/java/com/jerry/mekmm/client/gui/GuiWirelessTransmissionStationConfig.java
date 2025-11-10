package com.jerry.mekmm.client.gui;

import com.jerry.mekmm.client.gui.window.connect.GuiTransmissionStationList;
import com.jerry.mekmm.common.MoreMachineLang;
import com.jerry.mekmm.common.attachments.ConnectionConfig;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.client.gui.tooltip.TooltipUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.PacketUtils;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.button.PacketTileButtonPress;
import mekanism.common.util.text.InputValidator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiWirelessTransmissionStationConfig extends GuiConnectListHolder<TileEntityWirelessTransmissionStation, MekanismTileContainer<TileEntityWirelessTransmissionStation>>{

    private final int maxHeightLength;
    private GuiTextField energyRateField, fluidsRateField, chemicalsRateField, itemsRateField;

    public GuiWirelessTransmissionStationConfig(MekanismTileContainer<TileEntityWirelessTransmissionStation> container, Inventory inv, Component title) {
        super(container, inv, title);
        maxHeightLength = Math.max(Integer.toString(level.getMinBuildHeight()).length(), Integer.toString(level.getMaxBuildHeight() - 1).length());
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new MekanismImageButton(this, 5, 5, 11, 14, getButtonLocation("back"),
                (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketTileButtonPress(PacketTileButtonPress.ClickedTileButton.BACK_BUTTON, ((GuiWirelessTransmissionStationConfig) element.gui()).tile))))
                .setTooltip(TooltipUtils.BACK);

        energyRateField = addRenderableWidget(new GuiTextField(this, 13, 45, 38, 11));
        energyRateField.setMaxLength(Integer.toString(MekanismConfig.general.minerMaxRadius.get()).length());
        energyRateField.setInputValidator(InputValidator.DIGIT);
        energyRateField.configureDigitalBorderInput(() -> setText(energyRateField, PacketGuiInteract.GuiInteraction.SET_RADIUS));
        fluidsRateField = addRenderableWidget(new GuiTextField(this, 13, 71, 38, 11));
        fluidsRateField.setMaxLength(maxHeightLength);
        fluidsRateField.setInputValidator(InputValidator.DIGIT_OR_NEGATIVE);
        fluidsRateField.configureDigitalBorderInput(() -> setText(fluidsRateField, PacketGuiInteract.GuiInteraction.SET_MIN_Y));
        chemicalsRateField = addRenderableWidget(new GuiTextField(this, 13, 98, 38, 11));
        chemicalsRateField.setMaxLength(maxHeightLength);
        chemicalsRateField.setInputValidator(InputValidator.DIGIT_OR_NEGATIVE);
        chemicalsRateField.configureDigitalBorderInput(() -> setText(chemicalsRateField, PacketGuiInteract.GuiInteraction.SET_MAX_Y));
        itemsRateField = addRenderableWidget(new GuiTextField(this, 13, 125, 38, 11));
        itemsRateField.setMaxLength(maxHeightLength);
        itemsRateField.setInputValidator(InputValidator.DIGIT_OR_NEGATIVE);
        itemsRateField.configureDigitalBorderInput(() -> setText(itemsRateField, PacketGuiInteract.GuiInteraction.SET_MAX_Y));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
        renderTitleTextWithOffset(guiGraphics, 14);//Adjust spacing for back button
        drawScreenText(guiGraphics, MoreMachineLang.WTS_ENERGY_RATE.translate(tile.getEnergyRate()), 18);
        drawScreenText(guiGraphics, MoreMachineLang.WTS_FLUIDS_RATE.translate(tile.getFluidsRate()), 44);
        drawScreenText(guiGraphics, MoreMachineLang.WTS_CHEMICALS_RATE.translate(tile.getChemicalsRate()), 71);
        drawScreenText(guiGraphics, MoreMachineLang.WTS_ITEMS_RATE.translate(tile.getItemsRate()), 99);
    }

    @Override
    protected void onClick(ConnectionConfig config, int index) {
        //点击右侧按钮后执行
        addWindow(GuiTransmissionStationList.create(this, tile, level, config));
    }

    private void setText(GuiTextField field, PacketGuiInteract.GuiInteraction interaction) {
        if (!field.getText().isEmpty()) {
            try {
                PacketUtils.sendToServer(new PacketGuiInteract(interaction, tile, Integer.parseInt(field.getText())));
            } catch (NumberFormatException ignored) {//Might not be valid if multiple negative signs
            }
            field.setText("");
        }
    }
}
