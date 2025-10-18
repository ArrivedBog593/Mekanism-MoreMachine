package com.jerry.mekmm.client;

import com.jerry.mekaf.client.gui.machine.GuiAdvancedFactory;
import com.jerry.mekaf.common.registries.AdvancedFactoryContainerTypes;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.gui.machine.*;
import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
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
            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.RECYCLER, GuiRecycler::new);
            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.PLANTING_STATION, GuiPlantingStation::new);

            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.CNC_STAMPER, GuiStamper::new);
            ClientRegistrationUtil.registerElectricScreen(MoreMachineContainerTypes.CNC_LATHE);
            ClientRegistrationUtil.registerElectricScreen(MoreMachineContainerTypes.CNC_ROLLING_MILL);

            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.REPLICATOR, GuiReplicator::new);
            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.FLUID_REPLICATOR, GuiFluidReplicator::new);

            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.AMBIENT_GAS_COLLECTOR, GuiAmbientGasCollector::new);

            ClientRegistrationUtil.registerScreen(MoreMachineContainerTypes.MM_FACTORY, GuiMoreMachineFactory::new);
            ClientRegistrationUtil.registerScreen(AdvancedFactoryContainerTypes.ADVANCED_FACTORY, GuiAdvancedFactory::new);
        });
    }
}
