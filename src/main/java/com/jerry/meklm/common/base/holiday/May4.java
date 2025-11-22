package com.jerry.meklm.common.base.holiday;

import java.time.Month;

public class May4 extends Holiday {

    public static final May4 INSTANCE = new May4();

    private May4() {
        super(new YearlyDate(Month.MAY, 4));
    }
}
