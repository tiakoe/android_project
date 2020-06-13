package com.a.ndk_module;

public class CalUtil {

    static {
        System.loadLibrary("cal-lib");
    }

    public native int getNumber();

    public native String getMsg();
}
