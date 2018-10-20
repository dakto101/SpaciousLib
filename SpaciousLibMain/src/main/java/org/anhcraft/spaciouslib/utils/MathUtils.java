package org.anhcraft.spaciouslib.utils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A utility math class
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
     * Round a number up to 2 decimal places<br>
     * Source: <a href="https://stackoverflow.com/a/11701527">https://stackoverflow.com/a/11701527</a>
     * @param num the number
     * @return the rounded number
     */
    public static double round(double num){
        return (double) Math.round(num * 100) / 100;
    }

    /**
     * Check whether the given number is prime
     * @param num number
     * @return true if yes
     */
    public static boolean isPrime(int num){
        if(num < 2){
            return false;
        }
        int i = 2;
        int m = num == 2 ? 3 : num/2+1;
        while(i < m){
            if(num % i == 0){
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Check whether the given number is palindromic prime
     * @param num number
     * @return true if yes
     */
    public static boolean isPalprime(int num){
        return num == Integer.parseInt(new String(CommonUtils.reverse(Integer.toString(num).toCharArray())));
    }
}
