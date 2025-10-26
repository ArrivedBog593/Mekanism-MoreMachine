package com.jerry.mekmm.common.util;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.tier.FactoryTier;

public class MoreMachineEnumUtils {

    private MoreMachineEnumUtils() {

    }

    /**
     * Cached value of {@link MoreMachineFactoryType#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final MoreMachineFactoryType[] MM_FACTORY_TYPES = MoreMachineFactoryType.values();

    /**
     * Cached value of {@link AdvancedFactoryType#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final AdvancedFactoryType[] ADVANCED_FACTORY_TYPES = AdvancedFactoryType.values();

    /**
     * Cached value of {@link EMFactoryTier()}(If you load it). DO NOT MODIFY THIS LIST.
     */
    public static final FactoryTier[] EM_TIERS = new FactoryTier[]{EMFactoryTier.OVERCLOCKED, EMFactoryTier.QUANTUM, EMFactoryTier.DENSE, EMFactoryTier.MULTIVERSAL, EMFactoryTier.CREATIVE};
}
