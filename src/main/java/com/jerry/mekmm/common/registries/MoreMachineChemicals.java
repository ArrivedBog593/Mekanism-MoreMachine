package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.MoreMachineChemicalConstants;
import mekanism.api.chemical.Chemical;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;

public class MoreMachineChemicals {

    private MoreMachineChemicals() {
    }

    public static final ChemicalDeferredRegister MM_CHEMICALS = new ChemicalDeferredRegister(Mekmm.MOD_ID);

    public static final DeferredChemical<Chemical> NUTRITIONAL_PASTE = MM_CHEMICALS.register(MoreMachineChemicalConstants.NUTRITIONAL_PASTE);
    public static final DeferredChemical<Chemical> NUTRIENT_SOLUTION = MM_CHEMICALS.register(MoreMachineChemicalConstants.NUTRIENT_SOLUTION);
    public static final DeferredChemical<Chemical> UU_MATTER = MM_CHEMICALS.register(MoreMachineChemicalConstants.UU_MATTER);
    public static final DeferredChemical<Chemical> UNSTABLE_DIMENSIONAL_GAS = MM_CHEMICALS.register(MoreMachineChemicalConstants.UNSTABLE_DIMENSIONAL_GAS);
}
