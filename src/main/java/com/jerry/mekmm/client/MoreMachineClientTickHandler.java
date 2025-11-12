package com.jerry.mekmm.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class MoreMachineClientTickHandler {

    @SubscribeEvent
    public void onTick(ClientTickEvent.Pre event) {
        BlockHighlightManager.getInstance().tick();
    }
}
