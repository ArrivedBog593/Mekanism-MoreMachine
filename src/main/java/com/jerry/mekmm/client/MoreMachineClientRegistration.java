package com.jerry.mekmm.client;

import com.jerry.mekaf.client.gui.machine.GuiAdvancedFactory;
import com.jerry.mekaf.common.registries.AdvancedFactoryContainerTypes;
import com.jerry.meklm.client.gui.machine.GuiLargeRotaryCondensentrator;
import com.jerry.meklm.common.registries.LargeMachineBlocks;
import com.jerry.meklm.common.registries.LargeMachineContainerTypes;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.gui.GuiWirelessChargingStation;
import com.jerry.mekmm.client.gui.machine.*;
import com.jerry.mekmm.common.registries.MoreMachineBlocks;
import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static mekanism.client.ClientRegistration.addCustomModel;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT)
public class MoreMachineClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        addCustomModel(MoreMachineBlocks.WIRELESS_CHARGING_STATION, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
        addCustomModel(LargeMachineBlocks.LARGE_ROTARY_CONDENSENTRATOR, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.RECYCLER, GuiRecycler::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.PLANTING_STATION, GuiPlantingStation::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.CNC_STAMPER, GuiStamper::new);
        ClientRegistrationUtil.registerElectricScreen(event, MoreMachineContainerTypes.CNC_LATHE);
        ClientRegistrationUtil.registerElectricScreen(event, MoreMachineContainerTypes.CNC_ROLLING_MILL);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.REPLICATOR, GuiReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.FLUID_REPLICATOR, GuiFluidReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.CHEMIcAL_REPLICATOR, GuiChemicalReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.AMBIENT_GAS_COLLECTOR, GuiAmbientGasCollector::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.WIRELESS_CHARGING_STATION, GuiWirelessChargingStation::new);
        ClientRegistrationUtil.registerScreen(event, MoreMachineContainerTypes.MM_FACTORY, GuiMMFactory::new);

        //Advanced Factory
        ClientRegistrationUtil.registerScreen(event, AdvancedFactoryContainerTypes.ADVANCED_FACTORY, GuiAdvancedFactory::new);

        //Large Machine
        ClientRegistrationUtil.registerScreen(event, LargeMachineContainerTypes.LARGE_ROTARY_CONDENSENTRATOR, GuiLargeRotaryCondensentrator::new);
    }
}
