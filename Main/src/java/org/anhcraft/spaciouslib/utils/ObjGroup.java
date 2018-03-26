package org.anhcraft.spaciouslib.utils;

public class ObjGroup<A, B> {
    private A a;
    private B b;

    public ObjGroup(A a, B b){
        this.a = a;
        this.b = b;
    }

    public A getA(){
        return this.a;
    }

    public B getB(){
        return this.b;
    }
}