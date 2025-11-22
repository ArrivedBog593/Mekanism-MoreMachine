package com.jerry.meklm.common.base.holiday;

import java.time.Month;

public class AprilFools extends Holiday {

    public static final AprilFools INSTANCE = new AprilFools();

    private AprilFools() {
        super(new YearlyDate(Month.APRIL, 1));
    }
}
