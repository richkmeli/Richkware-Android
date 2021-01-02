package it.richkmeli.richkware.util;

import android.util.Log;

import it.richkmeli.richkware.BuildConfig;

public class Logger {
    public static final Boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String message) {
        if (DEBUG) {
            Log.println(Log.INFO, getTag(), message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.println(Log.INFO, getTag(), message);
        }
    }

    public static void e(String message, Throwable throwable) {
        if (DEBUG) {
            Log.println(Log.INFO, getTag(), message + " || " + throwable.getMessage());
            //throwable.printStackTrace();
        }
    }

    private static String getTag() {
        String className = new Exception().getStackTrace()[2].getClassName();
        return "LOG::" + className.substring(1 + className.lastIndexOf('.'));
    }
}
