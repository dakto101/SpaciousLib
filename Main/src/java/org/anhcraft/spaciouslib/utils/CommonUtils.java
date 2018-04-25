package org.anhcraft.spaciouslib.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils {
    /**
     * Parses the given string as an integer number safely.
     * @param number a number in string
     * @return the number
     */
    public static int toIntegerNumber(String number){
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
    public static String toBase64(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = str.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes the given base64 encoded string into a string
     * @param str the base64 encoded string
     * @return the string
     */
    public static String fromBase64(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }

    /**
     * Gets a object from the given array based on the string representation of the wanted object
     * @param a the string representation of a object which you want to get
     * @param array the array which contains that object
     * @return the object
     */
    public static <X> X getObject(String a, X[] array) {
        for(X x : array) {
            if(x.toString().equals(a)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Gets a object from the given list based on the string representation of the wanted object
     * @param a the string representation of a object which you want to get
     * @param list the list which contains that object
     * @return the object
     */
    public static <X> X getObject(String a, List<X> list){
        for(X x : list){
            if(x.toString().equals(a)){
                return x;
            }
        }
        return null;
    }

    /**
     * A simple method helps you to check is the given string valid in JSON format by using RegEx (Regular expression)
     * @param json the string which you want to check.
     * @return true if that string is a valid JSON string
     */
    public static boolean isValidJSON(String json) {
        return RegEx.JSON.matches(json);
    }

    /**
     * Divides objects to each "page" based on the index
     *
     * @param all list of all objects
     * @param index the index of "page" (start from 0)
     * @param max maximum amount of objects in each "page"
     * @return all objects in that "page"
     */
    public static <X> List<X> getPageItems(List<X> all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        List<X> filter = new ArrayList<>();
        int i = 0;
        for(X v : all){
            if(first <= i && i <= end){
                filter.add(v);
            }
            i++;
        }
        return filter;
    }

    /**
     * Converts all elements of a list to a new array
     * @param list list of elements
     * @param class_ the class of the elements
     * @return the new array
     */
    public static <X> X[] toArray(List<X> list, Class class_){
        return list.toArray((X[]) Array.newInstance(class_, list.size()));
    }

    /**
     * Converts all elements of an array to a new list
     * @param array the array which contains these elements
     * @return the new list
     */
    public static <X> List<X> toList(X[] array){
        return new ArrayList<>(Arrays.asList(array));
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
     * Shuffles
     * @param array
     */
    public static <T> T[] shuffle(T[] array) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            T temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}