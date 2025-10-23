package com.jerry.mekmm.common;

import com.jerry.mekmm.Mekmm;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

@NothingNullByDefault
public enum MoreMachineLang implements ILangEntry {
    //Gui lang strings
    MEKANISM_MORE_MACHINE("constants", "more_machine"),
    MEKANISM_LARGE_MACHINE("constants", "large_machine"),
    //Charging
    CHARGING_EQUIPS("charging", "equips"),
    CHARGING_INVENTORY("charging", "inventory"),
    CHARGING_CURIOS("charging", "curios"),
    //Factory Type
    RECYCLING("factory", "recycling"),
    PLANTING("factory", "planting"),
    STAMPING("factory", "stamping"),
    LATHING("factory", "lathing"),
    ROLLING_MILL("factory", "rolling_mill"),
    REPLICATING("factory", "replicating"),
    //Advanced Factory Type
    OXIDIZING("factory", "oxidizing"),
    CHEMICAL_INFUSING("factory", "chemical_infusing"),
    DISSOLVING("factory", "dissolving"),
    WASHING("factory", "washing"),
    CRYSTALLIZING("factory", "crystallizing"),
    PRESSURISED_REACTING("factory", "pressurised_reacting"),
    CENTRIFUGING("factory", "centrifuging"),
    LIQUIFYING("factory", "liquifying"),
    //Connector
    CONNECTOR_FROM("connector", "from"),
    CONNECTOR_TO("connector", "to"),
    CONNECTOR_DISCONNECT("connector", "disconnect"),
    CONNECTOR_FAIL("connector", "fail"),
    CONNECTOR_CLEARED("connector", "cleared"),
    CONNECTOR_LOSE("connector", "lose"),
    //Descriptions
    DESCRIPTION_RECYCLER("description", "recycler"),
    DESCRIPTION_PLANTING_STATION("description", "planting_station"),
    DESCRIPTION_CNC_STAMPER("description", "cnc_stamper"),
    DESCRIPTION_CNC_LATHE("description", "cnc_lathe"),
    DESCRIPTION_CNC_ROLLING_MILL("description", "cnc_rolling_mill"),
    DESCRIPTION_REPLICATOR("description", "replicator"),
    DESCRIPTION_FLUID_REPLICATOR("description", "fluid_replicator"),
    DESCRIPTION_CHEMicAL_REPLICATOR("description", "chemical_replicator"),
    DESCRIPTION_AMBIENT_GAS_COLLECTOR("description", "ambient_gas_collector"),
    DESCRIPTION_WIRELESS_CHARGING_STATION("description", "wireless_charging_station"),
    AUTHOR_DOLL("description", "author_doll"),
    //JEI
    RECIPE_VIEWER_INFO_UNSTABLE_DIMENSIONAL_GAS("info", "jei.unstable_dimensional_gas"),
    //Tooltip stuff
    IS_BLOCKING("tooltip", "is_blocking"),
    NO_BLOCKING("tooltip", "no_blocking");

    private final String key;

    MoreMachineLang(String type, String path) {
        this(Util.makeDescriptionId(type, Mekmm.rl(path)));
    }

    MoreMachineLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
