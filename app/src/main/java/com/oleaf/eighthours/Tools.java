package com.oleaf.eighthours;

public final class Tools{
    public static Object[] removeIndex(Object[] array, int index){
        Object[] a = new Object[array.length-1];
        System.arraycopy(array, 0, a, 0, index);
        System.arraycopy(array, index+1, a, index, array.length-index-1);
        return a;
    }
    public static int clamp(int v, int min, int max){ return (v < min) ? min : ((v > max) ? max : v);}
    public static float clamp(float v, float min, float max) {return (v < min) ? min : ((v > max) ? max : v);}
}
