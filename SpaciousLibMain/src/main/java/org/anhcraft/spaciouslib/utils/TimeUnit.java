package org.anhcraft.spaciouslib.utils;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public enum TimeUnit {
    MILLISECOND(0.001),
    SECOND(1),
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
     * Ex: 100 hours to weeks, calls: convert(TimeUnit.HOUR, 1000, TimeUnit.WEEK)
     * @param origin the original unit
     * @param duration the duration
     * @param to the destination unit
     * @return the amount of the destination unit
     */
    public static double convert(TimeUnit origin, double duration, TimeUnit to){
        return duration*origin.getSeconds()/to.getSeconds();
    }

    /**
     * Format the given time.<br>
     * Normally, the origin time will be seconds and then formatting it into hours, minutes and seconds. In that situation, the hour is the maximum unit, seconds is the minimum unit and also is the origin unit.<br>
     * There are some rules:<br>
     * - The duration can't be less than 0<br>
     * - The minimum unit can't be equals or greater than the maximum<br>
     * - The maximum unit must be greater than the origin
     * @param origin the original unit
     * @param duration the duration
     * @param min the minimum unit of time
     * @param max the maximum unit of time
     * @return a sorted map (from lowest unit to highest unit) contains units and their values, using this map to make a friendly string
     */
    public static TreeMap<TimeUnit, Integer> format(TimeUnit origin, int duration, TimeUnit min, TimeUnit max){
        ExceptionThrower.ifTrue(duration < 0, new Exception("Duration must be a positive integer"));
        ExceptionThrower.ifTrue(min.getSeconds() >= max.getSeconds(), new Exception("The minimum unit can't be equals or greater than the maximum"));
        ExceptionThrower.ifFalse(max.getSeconds() > origin.getSeconds(), new Exception("The maximum unit must be greater than the origin"));
        duration = (int) (duration*origin.getSeconds());
        TreeMap<TimeUnit, Integer> map = new TreeMap<>(Comparator.comparingDouble(TimeUnit::getSeconds));
        TimeUnit[] val = CommonUtils.reverse(values());
        for(TimeUnit unit : val){
            if(min.getSeconds() > unit.getSeconds()){
                map.put(map.lastKey(), map.lastEntry().getValue()+duration);
                break;
            }
            if(max.getSeconds() >= unit.getSeconds()){
                int x = (int) TimeUnit.convert(TimeUnit.SECOND, duration, unit);
                duration -= x*unit.getSeconds();
                map.put(unit, x);
            }
        }
        return map;
    }

    /**
     * Format the given time.<br>
     * Normally, the origin time will be seconds and then formatting it into hours, minutes and seconds. In that situation, the hour is the maximum unit, seconds is the minimum unit and also is the origin unit.<br>
     * There are some rules:<br>
     * - The duration can't be less than 0<br>
     * - The minimum unit can't be equals or greater than the maximum<br>
     * - The maximum unit must be greater than the origin
     * @param origin the original unit
     * @param duration the duration
     * @param min the minimum unit of time
     * @param max the maximum unit of time
     * @param ignore array of ignoring units
     * @return a sorted map (from lowest unit to highest unit) contains units and their values, using this map to make a friendly string
     */
    public static TreeMap<TimeUnit, Integer> format(TimeUnit origin, int duration, TimeUnit min, TimeUnit max, TimeUnit... ignore){
        ExceptionThrower.ifTrue(duration < 0, new Exception("Duration must be a positive integer"));
        ExceptionThrower.ifTrue(min.getSeconds() >= max.getSeconds(), new Exception("The minimum unit can't be equals or greater than the maximum"));
        ExceptionThrower.ifFalse(max.getSeconds() > origin.getSeconds(), new Exception("The maximum unit must be greater than the origin"));
        duration = (int) (duration*origin.getSeconds());
        TreeMap<TimeUnit, Integer> map = new TreeMap<>(Comparator.comparingDouble(TimeUnit::getSeconds));
        TimeUnit[] val = CommonUtils.reverse(values());
        List<TimeUnit> ignores = CommonUtils.toList(ignore);
        for(TimeUnit unit : val){
            if(ignores.contains(unit)){
                continue;
            }
            if(min.getSeconds() > unit.getSeconds()){
                map.put(map.lastKey(), map.lastEntry().getValue()+duration);
                break;
            }
            if(max.getSeconds() >= unit.getSeconds()){
                int x = (int) TimeUnit.convert(TimeUnit.SECOND, duration, unit);
                duration -= x*unit.getSeconds();
                map.put(unit, x);
            }
        }
        return map;
    }
}
