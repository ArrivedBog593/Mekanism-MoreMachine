package com.jerry.mekmm.common.attachments.component;

import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Map;

public class MMAttachedSideConfig {

    public static final AttachedSideConfig FLUID_REPLICATOR = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.MACHINE);
        configInfo.put(TransmissionType.CHEMICAL, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        configInfo.put(TransmissionType.FLUID, AttachedSideConfig.LightConfigInfo.OUT_EJECT);
        configInfo.put(TransmissionType.ENERGY, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });
}
