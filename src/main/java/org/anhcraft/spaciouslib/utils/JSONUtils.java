package org.anhcraft.spaciouslib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONUtils {
    public static boolean isValid(String json) {
        try {
            new Gson().fromJson(json, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static <C> String toJson(C type, Object object){
        return new Gson().toJson(object, new TypeToken<C>(){}.getType());
    }

    public static <C> Object toJson(C type, String json){
        return new Gson().fromJson(json, new TypeToken<C>(){}.getType());
    }
}
