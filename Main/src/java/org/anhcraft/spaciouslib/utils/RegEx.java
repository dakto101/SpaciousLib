package org.anhcraft.spaciouslib.utils;

import java.util.regex.Pattern;

/**
 * A utility class which has useful RegEx checkers
 */
public enum RegEx {
    JSON("^(\\{)(|.)(\\})$"),
    URL("(https?|ftp):(/{1,})((?!-)([-a-zA-Z0-9]{1,})(?<!-))\\.((?!-)([-a-zA-Z0-9]{1,})).*"),
    EMAIL("[A-Za-z0-9\\.\\-\\_]+@[A-Za-z0-9\\-\\_]+\\.[A-Za-z0-9\\-\\_]+"),
    REAL_NUMBER("(^(-|)[0-9]+$)|(^(-|)[0-9]+\\.[0-9]+$)"),
    INTEGER_NUMBER("^(-|)[0-9]+$"),
    UUID("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private String regex;

    RegEx(String regex){
         this.regex = regex;
    }

    public String getRegEx(){
        return this.regex;
    }

    /**
     * Checks does the given string match the RegEx
     * @param str the string
     * @return true if matches
     */
    public boolean matches(String str){
        return Pattern.compile(this.regex).matcher(str).matches();
    }
}
