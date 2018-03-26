package org.anhcraft.spaciouslib.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * A utility class for string
 */
public class StringUtils {
    /**
     * Parses the given string as an integer number safely.
     * @param num the number in string
     * @return the number
     */
    public static int toIntegerNumber(String num){
        return Integer.parseInt(num.replaceAll("[^\\d\\-]", ""));
    }

    /**
     * Parses the given string as a real number safely.
     * @param num the number in string
     * @return the number
     */
    public static double toRealNumber(String num){
        return Double.parseDouble(num.replaceAll("[^\\d\\-\\.]", ""));
    }

    /**
     * Encodes a string into base64 encoded string
     * @param str the string
     * @return base64 encoded string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String toBase64(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = str.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes the given base64 encoded string into "real" string
     * @param str the base64 encoded string
     * @return the string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
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
    public static <X> X get(String a, X[] array) {
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
    public static <X> X get(String a, List<X> list){
        for(X x : list){
            if(x.toString().equals(a)){
                return x;
            }
        }
        return null;
    }
}
