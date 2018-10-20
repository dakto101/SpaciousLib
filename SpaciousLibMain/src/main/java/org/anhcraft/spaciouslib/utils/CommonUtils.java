package org.anhcraft.spaciouslib.utils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils {
    /**
     * Parses the given string as an integer safely.
     * @param number a number in string
     * @return the number
     */
    public static int toInteger(String number){
        return Integer.parseInt(number.replaceAll("[^\\d\\-]", ""));
    }

    /**
     * Parses the given string as a real number safely.
     * @param number a number in string
     * @return the number
     */
    public static double toRealNumber(String number){
        return Double.parseDouble(number.replaceAll("[^\\d\\-\\.]", ""));
    }

    /**
     * Encodes the given string into a base64 encoded string
     * @param str the string
     * @return base64 encoded string
     */
    public static String toBase64(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes the given base64 encoded string into a string
     * @param str the base64 encoded string
     * @return the string
     */
    public static String fromBase64(String str) {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }

    /**
     * Gets a object from the given array based on the string representation of the wanted object
     * @param a the string representation of a object which you want to get
     * @param array the array which contains that object
     * @return the object
     */
    public static <E> E getObject(String a, E[] array) {
        for(E x : array) {
            if(x.toString().equals(a)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Gets a object from the given list based on the string representation of the wanted object
     * @param a the string representation of a object which you want to get
     * @param list the list
     * @return the object
     */
    public static <E> E getObject(String a, List<E> list){
        for(E x : list){
            if(x.toString().equals(a)){
                return x;
            }
        }
        return null;
    }

    /**
     * Gets a object from the given set based on the string representation of the wanted object
     * @param a the string representation of a object which you want to get
     * @param set the set
     * @return the object
     */
    public static <E> E getObject(String a, Set<E> set){
        for(E x : set){
            if(x.toString().equals(a)){
                return x;
            }
        }
        return null;
    }

    /**
     * A simple method to check whether the given string is a valid JSON
     * @param json the string
     * @return true if yes
     */
    public static boolean isValidJSON(String json) {
        return RegEx.JSON.matches(json);
    }

    /**
     * Divides the given array of objects to each "page" based on the index
     *
     * @param all array of objects
     * @param index the index of "page" (start from 0)
     * @param max maximum amount of objects in each "page"
     * @return all objects in that "page"
     */
    public static <E> E[] getPageItems(E[] all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        E[] filter = (E[]) Array.newInstance(all.getClass().getComponentType(), end-first+1);
        int i = 0;
        for(E v : all){
            if(first <= i && i <= end){
                filter[i] = v;
            }
            i++;
        }
        return filter;
    }

    /**
     * Divides the given list of objects to each "page" based on the index
     *
     * @param all list of objects
     * @param index the index of "page" (start from 0)
     * @param max maximum amount of objects in each "page"
     * @return all objects in that "page"
     */
    public static <E> List<E> getPageItems(List<E> all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        List<E> filter = new ArrayList<>();
        int i = 0;
        for(E v : all){
            if(first <= i && i <= end){
                filter.add(v);
            }
            i++;
        }
        return filter;
    }

    /**
     * Divides the given set of objects to each "page" based on the index
     *
     * @param all set of objects
     * @param index the index of "page" (start from 0)
     * @param max maximum amount of objects in each "page"
     * @return all objects in that "page"
     */
    public static <E> Set<E> getPageItems(Set<E> all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        Set<E> filter = new HashSet<>();
        int i = 0;
        for(E v : all){
            if(first <= i && i <= end){
                filter.add(v);
            }
            i++;
        }
        return filter;
    }

    /**
     * Converts a list to a new array
     * @param list list of elements
     * @param clazz the component type
     * @return the new array
     */
    public static <E> E[] toArray(List<E> list, Class clazz){
        return list.toArray((E[]) Array.newInstance(clazz, list.size()));
    }

    /**
     * Converts a set to a new array
     * @param set set of elements
     * @param clazz the component type
     * @return the new array
     */
    public static <E> E[] toArray(Set<E> set, Class clazz){
        return set.toArray((E[]) Array.newInstance(clazz, set.size()));
    }

    /**
     * Converts an array into a list
     * @param array an array
     * @return the new list
     */
    public static <E> List<E> toList(E[] array){
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Converts an array into a set
     * @param array an array
     * @return the new set
     */
    public static <E> Set<E> toSet(E[] array){
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * Creates a new UUID object from a UUID in string which doesn't have any dashes<br>
     * Source: https://stackoverflow.com/a/18987428
     * @return the UUID
     */
    public static UUID getUUIDWithoutDashes(String uuid){
        return UUID.fromString(uuid.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"));
    }

    /**
     * Shuffles the order of elements in an array
     * @param array an array
     * @return an ordered array
     */
    public static <E> E[] shuffle(E[] array) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            E temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }

    /**
     * Shuffles the order of elements in a list
     * @param list a list
     * @return an ordered list
     */
    public static <E> List<E> shuffle(List<E> list) {
        Collections.shuffle(list);
        return list;
    }

    /**
     * Shuffles the order of elements in a set
     * @param set a set
     * @return an ordered set
     */
    public static <E> Set<E> shuffle(Set<E> set) {
        return new HashSet<>(shuffle(new ArrayList<>(set)));
    }

    /**
     * Reverses the order of elements in a list
     * @param list a list
     * @return the reversed list
     */
    public static <E> List<E> reverse(List<E> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * Reverses the order of elements in a set
     * @param set a set
     * @return the reversed set
     */
    public static <E> Set<E> reverse(Set<E> set) {
        return new HashSet<>(reverse(new ArrayList<>(set)));
    }

    /**
     * Reverses the order of elements in an array
     * @param array an array
     * @return the reversed array
     */
    public static <E> E[] reverse(E[] array) {
        E[] narray = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        for(int i = 0; i < array.length; i++){
            narray[i] = array[array.length-i-1];
        }
        return narray;
    }
}