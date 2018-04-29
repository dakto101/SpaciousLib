package org.anhcraft.spaciouslib.utils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility class about randomization
 */
public class RandomUtils {

    /**
     * Picks a random element from the given list
     * @param list the list
     * @return the random element of that list
     */
    public static <X> X pickRandom(List<X> list) {
        if(list.size() == 0){
            return null;
        }
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * Picks a random element from the given array
     * @param array the array
     * @return the random element of that array
     */
    public static <X> X pickRandom(X[] array) {
        if(array.length == 0){
            return null;
        }
        return array[new Random().nextInt(array.length)];
    }

    /**
     * Gets a random integer number in specific range
     * @param min the minimum number
     * @param max the maximum number
     * @return the random integer number
     */
    public static int randomInt(int min, int max){
        max++;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Gets a random real number in specific range
     * @param min the minimum number
     * @param max the maximum number
     * @return the random real number
     */
    public static double randomDouble(double min, double max){
        max++;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
