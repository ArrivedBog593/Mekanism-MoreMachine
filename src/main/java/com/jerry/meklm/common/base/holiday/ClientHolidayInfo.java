package com.jerry.meklm.common.base.holiday;

import com.jerry.mekmm.Mekmm;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT)
public class ClientHolidayInfo {

    private ClientHolidayInfo() {}

    @SubscribeEvent
    public static void onStitch(TextureAtlasStitchedEvent event) {}
}
