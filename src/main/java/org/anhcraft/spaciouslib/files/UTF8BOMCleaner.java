package org.anhcraft.spaciouslib.files;

public class UTF8BOMCleaner {
    private static final String UTF8_BOM = "\uFEFF";

    /**
     * Removes all UTF-8 BOM characters in a string
     *
     * @param s a string
     *
     * @return a new string which doesn't have any UTF-8 BOM characters
     */
    public static String a(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
}
