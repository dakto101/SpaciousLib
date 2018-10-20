package org.anhcraft.spaciouslib.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    public static String escape(String text){
        return text.replace("\\", "\\\\").replace("'", "\\'").replace("\0", "\\0").replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\"").replace("\\x1a", "\\Z");
    }

    public static String hash(HashAlgorithm algorithm, String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm.getId());
        byte[] md = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte aMd : md) {
            sb.append(Integer.toString((aMd & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
