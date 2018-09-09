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

    /**
     * Converts the first time unit to the second<br>
     * Ex: 100 hours means ? weeks, calls: convert(TimeUnit.HOUR, 1000, TimeUnit.WEEK)
     * @param from the original unit
     * @param value the amount of the original unit
     * @param to the destination unit
     * @return the amount of the destination unit
     */
    public static double convert(TimeUnit from, double value, TimeUnit to){
        return from.getSeconds()*value/to.getSeconds();
    }
}
