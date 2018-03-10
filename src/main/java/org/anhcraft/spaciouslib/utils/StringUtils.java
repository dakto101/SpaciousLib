package org.anhcraft.spaciouslib.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class StringUtils {
    public static String toStrNumber(String num){
        return num.replaceAll("[^\\d.-]", "");
    }

    public static String toBase64(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = hash.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }
}
