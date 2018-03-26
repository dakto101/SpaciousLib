package org.anhcraft.spaciouslib.utils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A utility class about math
 */
public class MathUtils {

    /**
     * Evaluates or executes the given JavaScript code.<br>
     * Useful for calculating math expression (e.g: "5/2+3*8-(12+4)").
     * @param str math expression
     * @return the result
     */
    public static double eval(String str) {
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

    /**
     * Rounds a number up to 2 decimal places<br>
     * Source: <a href="https://stackoverflow.com/a/11701527">https://stackoverflow.com/a/11701527</a>
     * @param a the number
     * @return the rounded number
     */
    public static double round(double a){
        return (double) Math.round(a * 100) / 100;
    }
}
