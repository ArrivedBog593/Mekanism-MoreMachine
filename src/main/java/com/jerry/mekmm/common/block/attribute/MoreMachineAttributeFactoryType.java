package com.jerry.mekmm.common.block.attribute;

import com.jerry.mekmm.common.content.blocktype.MoreMachineFactoryType;

import mekanism.common.block.attribute.Attribute;

import org.jetbrains.annotations.NotNull;

public class MoreMachineAttributeFactoryType implements Attribute {

    private final MoreMachineFactoryType type;

    public MoreMachineAttributeFactoryType(MoreMachineFactoryType type) {
        this.type = type;
    }

    @NotNull
    public MoreMachineFactoryType getMoreMachineFactoryType() {
        return type;
    }
}
