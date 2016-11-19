package com.example.ienning.rxjavaandretrofitdemo;

/**
 * Created by ienning on 16-11-17.
 */

public class Testso {
    static {
        System.loadLibrary("jni-test");
    }
    public static String main() {
        Testso testso = new Testso();
        System.out.println(testso.get());
        testso.set("Hello jnis!");
        return testso.get();
    }
    public native String get();
    public native void set(String str);
}
