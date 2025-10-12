package com.jerry.mekmm.common.network;

import com.jerry.mekmm.common.network.to_server.MoreMachinePacketGuiInteract;
import mekanism.common.lib.Version;
import mekanism.common.network.BasePacketHandler;
import mekanism.common.network.to_client.configuration.SyncAllSecurityData;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

public class MoreMachinePacketHandler extends BasePacketHandler {

    public MoreMachinePacketHandler(IEventBus modEventBus, Version version) {
        super(modEventBus, version);
        modEventBus.addListener(RegisterConfigurationTasksEvent.class, event -> {
            ServerConfigurationPacketListener listener = event.getListener();
            event.register(new SyncAllSecurityData(listener));
        });
    }

    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(MoreMachinePacketGuiInteract.TYPE, MoreMachinePacketGuiInteract.STREAM_CODEC);
    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {

    }
}
