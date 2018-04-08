package org.anhcraft.spaciouslib.utils;

public enum TimeUnit {
    SECOND(1),
    MILLISECOND(SECOND.getSeconds() / 1000),
    MINUTE(SECOND.getSeconds() * 60),
    HOUR(MINUTE.getSeconds() * 60),
    DAY(HOUR.getSeconds() * 24),
    WEEK(DAY.getSeconds() * 7),
    MONTH_28(DAY.getSeconds() * 28),
    MONTH_29(DAY.getSeconds() * 29),
    MONTH_30(DAY.getSeconds() * 30),
    MONTH_31(DAY.getSeconds() * 31),
    YEAR(DAY.getSeconds() * 365),
    LEAP_YEAR(DAY.getSeconds() * 366),
    DECADE(YEAR.getSeconds() * 8 + LEAP_YEAR.getSeconds() * 2),
    CENTURY(DECADE.getSeconds() * 10),
    MILLENNIUM(CENTURY.getSeconds() * 10);

    private double seconds;

    TimeUnit(double seconds) {
        this.seconds = seconds;
    }

    public double getSeconds(){
        return this.seconds;
    }
}
