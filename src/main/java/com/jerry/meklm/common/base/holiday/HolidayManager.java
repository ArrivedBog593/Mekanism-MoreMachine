package com.jerry.meklm.common.base.holiday;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HolidayManager {

    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "HolidayManager Day Checker");
        t.setDaemon(true);
        return t;
    });

    private static final Set<Holiday> holidays = Set.of(
            May4.INSTANCE,
            AprilFools.INSTANCE);

    private HolidayManager() {}

    public static void init() {
        timer.scheduleAtFixedRate(HolidayManager::updateToday,
                LocalTime.now().until(LocalTime.MIDNIGHT, ChronoUnit.MILLIS),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.MILLISECONDS);
        updateToday();
    }

    private static void updateToday() {
        YearlyDate date = YearlyDate.now();
        for (Holiday holiday : holidays) {
            holiday.updateIsToday(date);
        }
    }
}
