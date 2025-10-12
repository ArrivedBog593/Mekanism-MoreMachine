package com.jerry.mekmm.common.block.attribute;

import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;
import mekanism.common.block.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

public class MMAttributeFactoryType implements Attribute {

    private final MoreMachineFactoryType type;

    public MMAttributeFactoryType(MoreMachineFactoryType type) {
        this.type = type;
    }

    @NotNull
    public MoreMachineFactoryType getMMFactoryType() {
        return type;
    }
}
