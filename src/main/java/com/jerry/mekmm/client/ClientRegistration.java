package com.jerry.mekmm.client;

import com.jerry.mekaf.client.gui.machine.GuiAdvancedFactory;
import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.gui.machine.*;
import com.jerry.mekmm.common.registries.MMContainerTypes;
import mekanism.client.ClientRegistrationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(MMContainerTypes.RECYCLER, GuiRecycler::new);
            ClientRegistrationUtil.registerScreen(MMContainerTypes.PLANTING_STATION, GuiPlantingStation::new);

            ClientRegistrationUtil.registerScreen(MMContainerTypes.CNC_STAMPER, GuiStamper::new);
            ClientRegistrationUtil.registerElectricScreen(MMContainerTypes.CNC_LATHE);
            ClientRegistrationUtil.registerElectricScreen(MMContainerTypes.CNC_ROLLING_MILL);

            ClientRegistrationUtil.registerScreen(MMContainerTypes.REPLICATOR, GuiReplicator::new);

            ClientRegistrationUtil.registerScreen(MMContainerTypes.MM_FACTORY, GuiMMFactory::new);
            ClientRegistrationUtil.registerScreen(AFContainerTypes.ADVANCED_FACTORY, GuiAdvancedFactory::new);
        });
    }
}
