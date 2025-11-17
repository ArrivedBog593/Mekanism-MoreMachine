package com.jerry.mekmm.client;

import com.jerry.mekmm.Mekmm;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Mekmm.MOD_ID, dist = Dist.CLIENT)
public class MoreMachineClient {

    public MoreMachineClient(ModContainer modContainer, IEventBus modEventBus) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
