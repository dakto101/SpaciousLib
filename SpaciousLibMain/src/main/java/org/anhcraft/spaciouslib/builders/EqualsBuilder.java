package org.anhcraft.spaciouslib.builders;

import java.lang.reflect.Field;

public class EqualsBuilder {
    private boolean equal = true;

    public EqualsBuilder append(Object a, Object b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        equal = a.equals(b);
        return this;
    }

    public EqualsBuilder append(Object a, Object b, boolean deep) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(!deep){
            return append(a, b);
        }
        if(a.getClass().isAssignableFrom(b.getClass())){
            equal = false;
            return this;
        }
        try {
            for(Field f : a.getClass().getDeclaredFields()) {
                if(!new EqualsBuilder().append(f.get(a), f.get(b)).equal) {
                    equal = false;
                    break;
                }
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public EqualsBuilder append(int a, int b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(double a, double b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(float a, float b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(byte a, byte b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(short a, short b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(boolean a, boolean b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(long a, long b) {
        if(!equal){
            return this;
        }
        equal = (a == b);
        return this;
    }

    public EqualsBuilder append(Object[] a, Object[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(Object oa : a){
            for(Object ob : b){
                if(!oa.equals(ob)){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(Object[] a, Object[] b, boolean deep) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(Object oa : a){
            for(Object ob : b){
                append(oa, ob, deep);
            }
        }
        return this;
    }

    public EqualsBuilder append(int[] a, int[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(int oa : a){
            for(int ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(double[] a, double[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(double oa : a){
            for(double ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(float[] a, float[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(float oa : a){
            for(float ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(byte[] a, byte[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(byte oa : a){
            for(byte ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(short[] a, short[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(short oa : a){
            for(short ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(boolean[] a, boolean[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(boolean oa : a){
            for(boolean ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(long[] a, long[] b) {
        if(!equal){
            return this;
        }
        if(a == null && b != null || a != null && b == null){
            equal = false;
            return this;
        }
        if(a.length != b.length) {
            equal = false;
            return this;
        }
        for(long oa : a){
            for(long ob : b){
                if(oa != ob){
                    equal = false;
                    break;
                }
            }
        }
        return this;
    }

    public boolean build() {
        return equal;
    }
}
