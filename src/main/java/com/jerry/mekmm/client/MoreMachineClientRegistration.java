package com.jerry.mekmm.client;

import com.jerry.mekaf.client.gui.machine.GuiAdvancedFactory;
import com.jerry.mekaf.common.registries.AFContainerTypes;
import com.jerry.meklm.client.gui.machine.GuiLargeRotaryCondensentrator;
import com.jerry.meklm.common.registries.LMBlocks;
import com.jerry.meklm.common.registries.LMContainerTypes;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.gui.machine.*;
import com.jerry.mekmm.common.registries.MMContainerTypes;
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
        addCustomModel(LMBlocks.LARGE_ROTARY_CONDENSENTRATOR, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.RECYCLER, GuiRecycler::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.PLANTING_STATION, GuiPlantingStation::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.CNC_STAMPER, GuiStamper::new);
        ClientRegistrationUtil.registerElectricScreen(event, MMContainerTypes.CNC_LATHE);
        ClientRegistrationUtil.registerElectricScreen(event, MMContainerTypes.CNC_ROLLING_MILL);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.REPLICATOR, GuiReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.FLUID_REPLICATOR, GuiFluidReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.CHEMIcAL_REPLICATOR, GuiChemicalReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.AMBIENT_GAS_COLLECTOR, GuiAmbientGasCollector::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.MM_FACTORY, GuiMMFactory::new);

        //Advanced Factory
        ClientRegistrationUtil.registerScreen(event, AFContainerTypes.ADVANCED_FACTORY, GuiAdvancedFactory::new);

        //Large Machine
        ClientRegistrationUtil.registerScreen(event, LMContainerTypes.LARGE_ROTARY_CONDENSENTRATOR, GuiLargeRotaryCondensentrator::new);
    }
}
