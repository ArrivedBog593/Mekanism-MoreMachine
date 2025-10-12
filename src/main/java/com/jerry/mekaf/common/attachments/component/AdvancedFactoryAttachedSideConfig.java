package com.jerry.mekaf.common.attachments.component;

import mekanism.api.RelativeSide;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.component.AttachedSideConfig.LightConfigInfo;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Map;

public class AdvancedFactoryAttachedSideConfig {

    public static final LightConfigInfo SINGLE_INPUT_2 = Util.make(() -> {
        Map<RelativeSide, DataType> sideConfig = new EnumMap<>(RelativeSide.class);
        sideConfig.put(RelativeSide.RIGHT, DataType.INPUT_2);
        sideConfig.put(RelativeSide.BACK, DataType.ENERGY);
        return new LightConfigInfo(sideConfig, false);
    });

    public static final AttachedSideConfig CHEMICAL_INFUSING = Util.make(() -> {
        Map<TransmissionType, LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, SINGLE_INPUT_2);
        configInfo.put(TransmissionType.CHEMICAL, LightConfigInfo.TWO_INPUT_AND_OUT);
        configInfo.put(TransmissionType.ENERGY, LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });

}
