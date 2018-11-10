package org.anhcraft.spaciouslib.utils;

/**
 * Represents a repeating operation. The repeater is same as loop statement but has side-effects.
 */
public abstract class Repeater {
    /**
     * Repeat an operation while the condition is true
     * @param init the initiation count
     * @param step the step count
     * @param repeater the repeater
     */
    public static void whileTrue(int init, int step, Repeater repeater){
        int i = init;
        while(repeater.check(i)){
            repeater.run(i);
            i += step;
        }
    }

    /**
     * Repeat an operation until the condition is true
     * @param init the initiation count
     * @param step the step count
     * @param repeater the repeater
     */
    public static void until(int init, int step, Repeater repeater){
        int i = init;
        while(!repeater.check(i)){
            repeater.run(i);
            i += step;
        }
    }

    /**
     * Execute the operation by calling this method
     * @param current the current count
     */
    public abstract void run(int current);

    /**
     * Check the given condition
     * @param current the current count
     * @return true or false
     */
    public abstract boolean check(int current);
}
