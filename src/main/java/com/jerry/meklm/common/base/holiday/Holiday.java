package com.jerry.meklm.common.base.holiday;

public abstract class Holiday {

    private final KnownDate date;
    private boolean isToday;

    public Holiday(KnownDate date) {
        this.date = date;
    }

    public final void updateIsToday(YearlyDate today) {
        isToday = date.isToday(today);
    }

    public final boolean isToday() {
        return isToday;
    }
}
