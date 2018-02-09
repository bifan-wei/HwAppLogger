package com.hw.applogger;


import android.util.Base64;

/**
 * created by ： bifan-wei
 */

public class LoggerEncrypt {

    public static String encode(String orig) {
        return new String(Base64.encode(orig.getBytes(), Base64.DEFAULT));
    }

    public static String decode(String orig) {
        return new String(Base64.decode(orig, Base64.DEFAULT));

    }
}
