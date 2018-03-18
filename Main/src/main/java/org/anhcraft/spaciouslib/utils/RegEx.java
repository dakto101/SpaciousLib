package org.anhcraft.spaciouslib.utils;

import java.util.regex.Pattern;

public enum RegEx {
    JSON("^(\\{)(|.)(\\})$"),
    URL("(https?|ftp):(/{1,})((?!-)([-a-zA-Z0-9]{1,})(?<!-))\\.((?!-)([-a-zA-Z0-9]{1,})).*"),
    EMAIL("[A-Za-z0-9\\.\\-\\_]+@[A-Za-z0-9\\-\\_]+\\.[A-Za-z0-9\\-\\_]+"),
    REAL_NUMBER("(^(-|)[0-9]+$)|(^(-|)[0-9]+\\.[0-9]+$)"),
    INTEGER_NUMBER("^(-|)[0-9]+$");

    private String regex;

    RegEx(String regex){
         this.regex = regex;
    }

    public String getRegEx(){
        return this.regex;
    }

    public boolean matches(String str){
        return Pattern.compile(this.regex).matcher(str).matches();
    }
}
