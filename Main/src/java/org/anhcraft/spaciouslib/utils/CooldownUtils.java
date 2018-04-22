package org.anhcraft.spaciouslib.utils;

import java.util.LinkedHashMap;

/**
 * A class helps you to manage cooldown times
 */
public class CooldownUtils {
    private static LinkedHashMap<String, Long> data = new LinkedHashMap<>();

    /**
     * Marks the current time as the begin of a cooldown
     * @param name the name of a cooldown
     */
    public static void mark(String name){
        data.put(name, System.currentTimeMillis());
    }

    /**
     * Checks is a cooldown expired after the given seconds.<br>
     * For example: you've marked the cooldown at 10:00:00.<br>
     * After 30 seconds:<br>
     * - If you check with a duration of 20 seconds, it will return true, because the end time of the cooldown was 10:00:20<br>
     * - If you check with a duration of 40 seconds, it will return false, because the end time of the cooldown will be 10:00:40. You've 10 seconds left!!!
     * @param name the name of the cooldown
     * @param seconds a duration in seconds
     * @return true if yes
     */
    public static boolean isTimeout(String name, int seconds){
        if(data.containsKey(name)){
            if((System.currentTimeMillis()-data.get(name)) < (seconds*1000)){
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the time left until the end time of the cooldown.<br>
     * For example: you've marked the cooldown at 5:30:00.<br>
     * After 10 seconds, if you get with a duration of 30 seconds, it will return 20, it means you've 20 seconds left!!!
     * @param name the name of the cooldown
     * @param seconds a duration in seconds
     * @return the time left
     */
    public static int timeLeft(String name, int seconds){
        if(data.containsKey(name)){
            return seconds - ((int) ((System.currentTimeMillis()-data.get(name))/1000));
        }
        return 0;
    }
}
