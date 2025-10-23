package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.item.ItemConnector.ConnectorMode;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registration.impl.DataComponentDeferredRegister;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;

@NothingNullByDefault
public class MoreMachineDataComponents {

    private MoreMachineDataComponents() {
    }

    public static final DataComponentDeferredRegister MM_DATA_COMPONENTS = new DataComponentDeferredRegister(Mekmm.MOD_ID);

    public static final MekanismDeferredHolder<DataComponentType<?>, DataComponentType<ConnectorMode>> CONNECTOR_MODE = MM_DATA_COMPONENTS.simple("connector_mode",
            builder -> builder.persistent(ConnectorMode.CODEC)
                    .networkSynchronized(ConnectorMode.STREAM_CODEC)
    );

    public static final MekanismDeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> CONNECT_FROM = MM_DATA_COMPONENTS.simple("connect_from",
            builder -> builder.persistent(GlobalPos.CODEC)
                    .networkSynchronized(GlobalPos.STREAM_CODEC)
    );

    public static final MekanismDeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CHARGE_EQUIPMENT = MM_DATA_COMPONENTS.registerBoolean("charge_equipment");
    public static final MekanismDeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CHARGE_INVENTORY = MM_DATA_COMPONENTS.registerBoolean("charge_inventory");
    public static final MekanismDeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CHARGE_CURIOS = MM_DATA_COMPONENTS.registerBoolean("charge_curios");

}
