package com.example.ienning.ndktest;

/**
 * Created by ienning on 16-11-17.
 */

public class Testso {
    static {
        System.loadLibrary("jni-testso");
    }
    public static void main(String args[]) {
        Testso testso = new Testso();
        System.out.println(testso.get());
        testso.set("Hello jnis!");
    }
    public native String get();
    public native void set(String str);
}
