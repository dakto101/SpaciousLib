package org.anhcraft.spaciouslib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * A utility class for JSON (JavaScript Object Notation)
 */
public class JSONUtils {
    /**
     * A simple method helps you to check is the given string valid in JSON format by using RegEx (Regular expression)
     * @param json the string which you want to check.
     * @return true if that string is a valid JSON string
     */
    public static boolean isValid(String json) {
        return RegEx.JSON.matches(json);
    }

    /**
     * Serializes an object to JSON string by using Gson
     * @param type the type of that object
     * @param object the object
     * @return the JSON string
     */
    public static <C> String toJson(C type, Object object){
        return new Gson().toJson(object, new TypeToken<C>(){}.getType());
    }

    /**
     * Deserializes a JSON string to object by using Gson
     * @param type the type of that object
     * @param json the json
     * @return the object
     */
    public static <C> Object fromJson(C type, String json){
        return new Gson().fromJson(json, new TypeToken<C>(){}.getType());
    }
}
