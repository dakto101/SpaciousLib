package org.anhcraft.spaciouslib.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A class helps you to manage cooldown times
 */
public class Cooldown {
    private long current;

    /**
     * Creates a new Cooldown instance
     */
    public Cooldown(){
        this.current = System.currentTimeMillis();
    }

    /**
     * Creates a new Cooldown instance
     * @param current the beginning time of this cooldown, in milliseconds
     */
    public Cooldown(long current){
        this.current = current;
    }

    /**
     * Resets the beginning time of this cooldown to make it equal with the current time
     * @return this object
     */
    public Cooldown reset(){
        this.current = System.currentTimeMillis();
        return this;
    }

    /**
     * Sets the ending time of this cooldown.<br>
     * And checks is the current time overpassed the cooldown.<br>
     * For example: you've reset the cooldown at 10:00:00.<br>
     * After 30 seconds:<br>
     * - If you check with a duration of 20 seconds, this method will return true, because the end time of the cooldown was 10:00:20<br>
     * - If you check with a duration of 40 seconds, this method will return false, because the end time of the cooldown will be 10:00:40. You've 10 seconds left!!!
     * @param seconds a duration in seconds
     * @return true if yes
     */
    public boolean isTimeout(double seconds){
        return !((System.currentTimeMillis() - this.current) < (seconds * 1000d));
    }

    /**
     * Gets the time left until the end time of the cooldown.<br>
     * For example: you've marked the cooldown at 5:30:00.<br>
     * After 10 seconds, if you get with a duration of 30 seconds, this method will return 20, it means you've 20 seconds left!!!
     * @param seconds a duration in seconds
     * @return the time left
     */
    public double timeLeft(double seconds){
        return seconds - ((System.currentTimeMillis()-this.current)/1000d);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Cooldown c = (Cooldown) o;
            return new EqualsBuilder()
                    .append(c.current, this.current)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(15, 43)
                .append(this.current).toHashCode();
    }
}
