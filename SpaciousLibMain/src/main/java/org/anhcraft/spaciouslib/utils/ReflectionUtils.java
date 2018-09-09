package org.anhcraft.spaciouslib.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class helps you to use reflection
 */
public class ReflectionUtils {
    public static Object cast(Class<?> clazz, Object castObj){
        return clazz.cast(castObj);
    }

    public static Object getEnum(String name, Class<?> clazz){
        return getStaticField(name, clazz);
    }

    public static Object getField(String field, Class<?> clazz, Object obj){
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(obj);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticField(String field, Class<?> clazz){
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(null);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setField(String field, Class<?> clazz, Object obj, Object value){
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(obj, value);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setStaticField(String field, Class<?> clazz, Object value){
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(null, value);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getStaticMethod(String method, Class<?> clazz, Group<Class<?>[], Object[]> args){
        try {
            Method f = clazz.getDeclaredMethod(method, args.getA());
            f.setAccessible(true);
            return f.invoke(null, args.getB());
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getMethod(String method, Class<?> clazz, Object obj, Group<Class<?>[], Object[]> args){
        try {
            Method f = clazz.getDeclaredMethod(method, args.getA());
            f.setAccessible(true);
            return f.invoke(obj, args.getB());
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticMethod(String method, Class<?> clazz){
        try {
            Method f = clazz.getDeclaredMethod(method);
            f.setAccessible(true);
            return f.invoke(null);
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getMethod(String method, Class<?> clazz, Object obj){
        try {
            Method f = clazz.getDeclaredMethod(method);
            f.setAccessible(true);
            return f.invoke(obj);
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getConstructor(Class<?> clazz, Group<Class<?>[], Object[]> args){
        try {
            Constructor<?> f = clazz.getDeclaredConstructor(args.getA());
            f.setAccessible(true);
            return f.newInstance(args.getB());
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getConstructor(Class<?> clazz){
        try {
            Constructor<?> f = clazz.getDeclaredConstructor();
            f.setAccessible(true);
            return f.newInstance();
        } catch( IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
