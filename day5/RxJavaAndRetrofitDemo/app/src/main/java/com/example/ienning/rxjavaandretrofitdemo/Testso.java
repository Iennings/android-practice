package com.example.ienning.rxjavaandretrofitdemo;

import android.provider.Settings;
import android.util.Log;

/**
 * Created by ienning on 16-11-17.
 */

public class Testso {
    static {
        System.loadLibrary("jni-test");
        System.loadLibrary("testso");
    }
    public static String main() {
        Testso testso = new Testso();
        Log.e("Ienning", "main: to load it if ok");
        testso.set("Hello jnis!");
        Log.e("Ienning", "Hello jnis to test");
        Log.e("Ienning", "main: to test get() if ok? this is Second" + testso.get());
        return testso.get();
    }
    public native String get();
    public native void set(String str);
}
