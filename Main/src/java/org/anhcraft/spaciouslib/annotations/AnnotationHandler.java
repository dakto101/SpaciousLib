package org.anhcraft.spaciouslib.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnotationHandler {
    private static HashMap<Class, List<Object>> data = new HashMap<>();

    public static void register(Class clazz, Object object){
        List<Object> x = new ArrayList<>();
        if(data.containsKey(clazz)){
            x = data.get(clazz);
        }
        x.add(object);
        data.put(clazz, x);
    }

    public static void unregister(Class clazz, Object object){
        List<Object> x = new ArrayList<>();
        if(data.containsKey(clazz)){
            x = data.get(clazz);
        }
        x.remove(object);
        data.put(clazz, x);
    }

    public static HashMap<Class, List<Object>> getClasses(){
        return data;
    }
}
