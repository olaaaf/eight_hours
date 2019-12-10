package com.oleaf.eighthours.settings;

import java.io.Serializable;

public class Volatile<T>  implements Serializable {
    private T variable;
    private Runnable r;

    Volatile(T v, Runnable runnable) {
        variable = v;
        r = runnable;
    }

    void set(T nV) {
        variable = nV;
        r.run();
    }

    T get() {
        return variable;
    }
}
