package com.jerry.mekmm.common.attachments.component;

import mekanism.api.RelativeSide;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Map;

public class MMAttachedSideConfig {

    public static final AttachedSideConfig FLUID_REPLICATOR = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.EXTRA_MACHINE);
        configInfo.put(TransmissionType.CHEMICAL, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        configInfo.put(TransmissionType.FLUID, AttachedSideConfig.LightConfigInfo.OUT_EJECT);
        configInfo.put(TransmissionType.ENERGY, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });

    public static final AttachedSideConfig CHEMICAL_REPLICATOR = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.EXTRA_MACHINE);
        configInfo.put(TransmissionType.CHEMICAL, AttachedSideConfig.LightConfigInfo.TWO_INPUT_AND_OUT);
        configInfo.put(TransmissionType.ENERGY, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });

    public static final AttachedSideConfig.LightConfigInfo TWO_INPUT_AND_OUT = Util.make(() -> {
        Map<RelativeSide, DataType> sideConfig = new EnumMap<>(RelativeSide.class);
        sideConfig.put(RelativeSide.RIGHT, DataType.INPUT_1);
        sideConfig.put(RelativeSide.LEFT, DataType.INPUT_2);
        sideConfig.put(RelativeSide.FRONT, DataType.OUTPUT);
        return new AttachedSideConfig.LightConfigInfo(sideConfig, true);
    });
}
