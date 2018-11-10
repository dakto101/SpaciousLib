package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.ArrayBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    public static String escape(String text){
        return text.replace("\\", "\\\\").replace("'", "\\'").replace("\0", "\\0").replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\"").replace("\\x1a", "\\Z");
    }

    public static String unescape(String text){
        return text.replace("\\\\\\", "\\").replace("\\'", "'").replace("\\0", "\0").replace("\\n", "\n").replace("\\r", "\r").replace("\\\"", "\"").replace("\\Z", "\\xla");
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

    public static String repeat(String str, int times){
        if(times == 0){
            return "";
        }
        if(times == 1){
            return str;
        }
        char[] chars = str.toCharArray();
        ArrayBuilder arr = new ArrayBuilder(char.class);
        Repeater.whileTrue(0, 1, new Repeater() {
            @Override
            public void run(int current) {
                arr.append(chars);
            }

            @Override
            public boolean check(int current) {
                return current < times;
            }
        });
        return new String((char[]) arr.build());
    }

    public static String removeNumericChars(String str){
        ArrayBuilder arr = new ArrayBuilder(char.class);
        char[] chars = str.toCharArray();
        for(char c : chars){
            if(c < 48 || c > 57){
                arr.append(c);
            }
        }
        return new String((char[]) arr.build());
    }

    public static String removeAlphabetChars(String str){
        ArrayBuilder arr = new ArrayBuilder(char.class);
        char[] chars = str.toCharArray();
        for(char c : chars){
            if(c < 65 || (c > 90 && c < 97) || c > 122){
                arr.append(c);
            }
        }
        return new String((char[]) arr.build());
    }

    public static String reverse(String str){
        ArrayBuilder arr = new ArrayBuilder(char.class);
        Repeater.whileTrue(0, 1, new Repeater() {
            @Override
            public void run(int current) {
                arr.append(str.charAt(str.length()-current-1));
            }

            @Override
            public boolean check(int current) {
                return current < str.length();
            }
        });
        return new String((char[]) arr.build());
    }
}
