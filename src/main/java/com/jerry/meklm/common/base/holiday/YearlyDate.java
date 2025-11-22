package com.jerry.meklm.common.base.holiday;

import org.jetbrains.annotations.Range;

import java.time.LocalDate;
import java.time.Month;

public record YearlyDate(Month month, @Range(from = 1, to = 31) int day) implements KnownDate {

    @Override
    public boolean isToday(YearlyDate today) {
        return equals(today);
    }

    public static YearlyDate now() {
        LocalDate time = LocalDate.now();
        return new YearlyDate(time.getMonth(), time.getDayOfMonth());
    }
}
