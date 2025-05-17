package com.jerry.mekmm.common.util;

import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;

public class MMEnumUtils {

    private MMEnumUtils() {

    }

    /**
     * Cached value of {@link MMFactoryType#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final MMFactoryType[] MM_FACTORY_TYPES = MMFactoryType.values();

    /**
     * Cached value of {@link AdvancedFactoryType#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final AdvancedFactoryType[] ADVANCED_FACTORY_TYPES = AdvancedFactoryType.values();
}
