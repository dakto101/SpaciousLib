package org.anhcraft.spaciouslib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class about encrypting
 */
public class EncryptUtils {
    public enum Type {
        MD2,
        MD5,
        SHA_1,
        SHA_256,
        SHA_384,
        SHA_512
    }

    /**
     * Encrypts a given string by using a specific hashing algorithm
     * @param type the type of hashing algorithm
     * @param string the string
     * @return the hash
     */
    public static String create(Type type, String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance(type.toString().replace("_", "-"));
        byte[] md = digest.digest(string.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for(byte aMd : md) {
            sb.append(Integer.toString((aMd & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
