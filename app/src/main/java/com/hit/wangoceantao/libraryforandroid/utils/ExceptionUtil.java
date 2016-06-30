package com.hit.wangoceantao.libraryforandroid.utils;

import android.os.Looper;

/**
 * Note:
 * Author: luqingyuan
 * Date: 2015/10/17.
 */
public class ExceptionUtil {
    public static void throwIfNotOnMainThread() {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("must be invocked from the main thread");
        }
    }

    public static void throwParameterNotFound(Object param) {
        throw new IllegalArgumentException("parameter not found: " + param);
    }

    public static void handleException(Exception e) {
        e.printStackTrace();
    }
}
