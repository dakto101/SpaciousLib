package org.anhcraft.spaciouslib.utils;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility class about randomization
 */
public class RandomUtils {
    /**
     * Picks a random element from the given set
     * @param set the set
     * @return a random element
     */
    public static <E> E pickRandom(Set<E> set) {
        if(set.size() == 0){
            return null;
        }
        return (E) set.toArray()[new Random().nextInt(set.size())];
    }

    /**
     * Picks a random element from the given list
     * @param list the list
     * @return a random element
     */
    public static <E> E pickRandom(List<E> list) {
        if(list.size() == 0){
            return null;
        }
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * Picks a random element from the given array
     * @param array the array
     * @return a random element
     */
    public static <E> E pickRandom(E[] array) {
        if(array.length == 0){
            return null;
        }
        return array[new Random().nextInt(array.length)];
    }

    /**
     * Generates a random integer in specific range
     * @param min the minimum number
     * @param max the maximum number
     * @return number
     */
    public static int randomInt(int min, int max){
        max++;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Generates a random real number in specific range
     * @param min the minimum number
     * @param max the maximum number
     * @return number
     */
    public static double randomDouble(double min, double max){
        max++;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Generates a random long number in specific range
     * @param min the minimum number
     * @param max the maximum number
     * @return number
     */
    public static double randomLong(long min, long max){
        max++;
        return ThreadLocalRandom.current().nextLong(min, max);
    }
}
