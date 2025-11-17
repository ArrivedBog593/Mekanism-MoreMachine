package com.jerry.mekmm.api;

import net.neoforged.neoforge.common.ItemAbility;

public final class MoreMachineItemAbilities {

    /**
     * Exposed by Connector that can currently connect chemical properties for a block.
     */
    public static final ItemAbility CONNECT_CHEMICALS = ItemAbility.get("connect_chemicals");
    /**
     * Exposed by Connector that can currently connect energy properties for a block.
     */
    public static final ItemAbility CONNECT_ENERGY = ItemAbility.get("connect_energy");
    /**
     * Exposed by Connector that can currently connect fluid properties for a block.
     */
    public static final ItemAbility CONNECT_FLUIDS = ItemAbility.get("connect_fluids");
    /**
     * Exposed by Connector that can currently connect heat properties for a block.
     */
    public static final ItemAbility CONNECT_HEAT = ItemAbility.get("connect_heat");
    /**
     * Exposed by Connector that can currently connect item properties for a block.
     */
    public static final ItemAbility CONNECT_ITEMS = ItemAbility.get("connect_items");
}
