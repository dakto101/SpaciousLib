package org.anhcraft.spaciouslib.utils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathUtils {
    public static double eval(final String str) {
        try {
            Object x = new ScriptEngineManager().getEngineByName("JavaScript").eval(str);
            if(x instanceof Integer){
                return (double) ((Integer) x);
            } else {
                return (double) x;
            }
        } catch(ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double round(double a){
        return (double) Math.round(a * 100) / 100;
    }
}
