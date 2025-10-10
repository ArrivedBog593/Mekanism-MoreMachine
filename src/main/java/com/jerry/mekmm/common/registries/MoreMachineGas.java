package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.MMChemicalConstants;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class MoreMachineGas {

    private MoreMachineGas() {
    }

    public static final GasDeferredRegister MM_GASES = new GasDeferredRegister(Mekmm.MOD_ID);

    public static final GasRegistryObject<Gas> UU_MATTER = MM_GASES.register(MMChemicalConstants.UU_MATTER);
}
