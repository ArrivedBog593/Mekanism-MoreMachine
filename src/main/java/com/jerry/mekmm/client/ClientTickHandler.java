package com.jerry.mekmm.client;

import com.jerry.mekmm.client.render.BlockHighlightManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class ClientTickHandler {

    @SubscribeEvent
    public void onTick(ClientTickEvent.Pre event) {
        BlockHighlightManager.getInstance().tick();
    }
}
