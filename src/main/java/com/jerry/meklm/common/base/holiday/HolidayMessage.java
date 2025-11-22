package com.jerry.meklm.common.base.holiday;

import net.minecraft.network.chat.Component;

import java.util.Arrays;

public record HolidayMessage(Component themedLines, Component... lines) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof HolidayMessage(Component themedLines1, Component[] lines1) && themedLines.equals(themedLines1) && Arrays.equals(lines, lines1);
    }

    @Override
    public int hashCode() {
        return 31 * themedLines.hashCode() + Arrays.hashCode(lines);
    }
}
