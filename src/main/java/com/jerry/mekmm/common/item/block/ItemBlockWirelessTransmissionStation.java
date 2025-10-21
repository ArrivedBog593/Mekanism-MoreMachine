package com.jerry.mekmm.common.item.block;

import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.component.AttachedSideConfig.LightConfigInfo;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.TileEntityChemicalTank;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Map;

public class ItemBlockWirelessTransmissionStation extends ItemBlockTooltip<BlockTileModel<TileEntityWirelessTransmissionStation, Machine<TileEntityWirelessTransmissionStation>>> {

    public static final AttachedSideConfig SIDE_CONFIG = Util.make(() -> {
        Map<TransmissionType, LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ENERGY, LightConfigInfo.FRONT_OUT_NO_EJECT);
        configInfo.put(TransmissionType.FLUID, LightConfigInfo.OUT_EJECT_LEFT);
        configInfo.put(TransmissionType.CHEMICAL, LightConfigInfo.RIGHT_OUTPUT);
        configInfo.put(TransmissionType.ITEM, LightConfigInfo.FRONT_OUT_NO_EJECT);
//        configInfo.put(TransmissionType.HEAT, LightConfigInfo.FRONT_OUT_NO_EJECT);
        return new AttachedSideConfig(configInfo);
    });

    public ItemBlockWirelessTransmissionStation(BlockTileModel<TileEntityWirelessTransmissionStation, Machine<TileEntityWirelessTransmissionStation>> block, Properties properties) {
        super(block, true, properties
                .component(MekanismDataComponents.DUMP_MODE, TileEntityChemicalTank.GasMode.IDLE)
                .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                .component(MekanismDataComponents.SIDE_CONFIG, SIDE_CONFIG)
        );
    }
}
