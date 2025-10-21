package com.jerry.mekmm;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT)
public class MoreMachineRender {

    @SubscribeEvent
    public static void onStitch(TextureAtlasStitchedEvent event) {

    }
}
