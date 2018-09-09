package org.anhcraft.spaciouslib.utils;

public class StringUtils {
    public static String escape(String text){
        return text.replace("\\", "\\\\").replace("'", "\\'").replace("\0", "\\0").replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\"").replace("\\x1a", "\\Z");
    }
}
