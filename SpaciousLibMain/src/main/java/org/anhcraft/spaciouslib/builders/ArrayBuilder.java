package org.anhcraft.spaciouslib.builders;

import java.lang.reflect.Array;

public class ArrayBuilder {
    private Object array;
    private Class<?> clazz;
    private int size;

    public ArrayBuilder(Class<?> clazz){
        this.clazz = clazz;
        array = Array.newInstance(clazz, 0);
        size = 0;
    }

    public ArrayBuilder append(Object obj){
        Object[] n = (Object[]) Array.newInstance(clazz, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = obj;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(Object... objs){
        Object[] n = (Object[]) Array.newInstance(clazz, size+objs.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(objs, 0, n, size, objs.length);
        array = n;
        size += objs.length;
        return this;
    }

    public ArrayBuilder append(int v){
        if(!clazz.isAssignableFrom(int.class)){
            return this;
        }
        int[] n = (int[]) Array.newInstance(int.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(int... ints){
        if(!clazz.isAssignableFrom(int.class)){
            return this;
        }
        int[] n = (int[]) Array.newInstance(int.class, size+ints.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(ints, 0, n, size, ints.length);
        array = n;
        size += ints.length;
        return this;
    }

    public ArrayBuilder append(char v){
        if(!clazz.isAssignableFrom(char.class)){
            return this;
        }
        char[] n = (char[]) Array.newInstance(char.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(char... chars){
        if(!clazz.isAssignableFrom(char.class)){
            return this;
        }
        char[] n = (char[]) Array.newInstance(char.class, size+chars.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(chars, 0, n, size, chars.length);
        array = n;
        size += chars.length;
        return this;
    }

    public ArrayBuilder append(boolean v){
        if(!clazz.isAssignableFrom(boolean.class)){
            return this;
        }
        boolean[] n = (boolean[]) Array.newInstance(boolean.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(boolean... booleans){
        if(!clazz.isAssignableFrom(boolean.class)){
            return this;
        }
        boolean[] n = (boolean[]) Array.newInstance(boolean.class, size+booleans.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(booleans, 0, n, size, booleans.length);
        array = n;
        size += booleans.length;
        return this;
    }

    public ArrayBuilder append(short v){
        if(!clazz.isAssignableFrom(short.class)){
            return this;
        }
        short[] n = (short[]) Array.newInstance(short.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(short... shorts){
        if(!clazz.isAssignableFrom(short.class)){
            return this;
        }
        short[] n = (short[]) Array.newInstance(short.class, size+shorts.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(shorts, 0, n, size, shorts.length);
        array = n;
        size += shorts.length;
        return this;
    }

    public ArrayBuilder append(long v){
        if(!clazz.isAssignableFrom(long.class)){
            return this;
        }
        long[] n = (long[]) Array.newInstance(long.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(long... longs){
        if(!clazz.isAssignableFrom(long.class)){
            return this;
        }
        long[] n = (long[]) Array.newInstance(long.class, size+longs.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(longs, 0, n, size, longs.length);
        array = n;
        size += longs.length;
        return this;
    }

    public ArrayBuilder append(double v){
        if(!clazz.isAssignableFrom(double.class)){
            return this;
        }
        double[] n = (double[]) Array.newInstance(double.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(double... doubles){
        if(!clazz.isAssignableFrom(double.class)){
            return this;
        }
        double[] n = (double[]) Array.newInstance(double.class, size+doubles.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(doubles, 0, n, size, doubles.length);
        array = n;
        size += doubles.length;
        return this;
    }

    public ArrayBuilder append(float v){
        if(!clazz.isAssignableFrom(float.class)){
            return this;
        }
        float[] n = (float[]) Array.newInstance(float.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(float... floats){
        if(!clazz.isAssignableFrom(float.class)){
            return this;
        }
        float[] n = (float[]) Array.newInstance(float.class, size+floats.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(floats, 0, n, size, floats.length);
        array = n;
        size += floats.length;
        return this;
    }

    public ArrayBuilder append(byte v){
        if(!clazz.isAssignableFrom(byte.class)){
            return this;
        }
        byte[] n = (byte[]) Array.newInstance(byte.class, size+1);
        System.arraycopy(array, 0, n, 0, size);
        n[size] = v;
        array = n;
        size++;
        return this;
    }

    public ArrayBuilder append(byte... bytes){
        if(!clazz.isAssignableFrom(byte.class)){
            return this;
        }
        byte[] n = (byte[]) Array.newInstance(byte.class, size+bytes.length);
        System.arraycopy(array, 0, n, 0, size);
        System.arraycopy(bytes, 0, n, size, bytes.length);
        array = n;
        size += bytes.length;
        return this;
    }

    public Object build(){
        return array;
    }
}
