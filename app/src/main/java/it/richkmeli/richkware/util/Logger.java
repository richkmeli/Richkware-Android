package it.richkmeli.richkware.util;

import it.richkmeli.richkware.BuildConfig;

public class Logger extends it.richkmeli.jframework.util.log.Logger {
    public static void info(String message) {
        debug = BuildConfig.DEBUG;
        it.richkmeli.jframework.util.log.Logger.info(message);
    }

    public static void warning(String message) {
        debug = BuildConfig.DEBUG;
        it.richkmeli.jframework.util.log.Logger.warning(message);
    }

    public static void error(String message) {
        debug = BuildConfig.DEBUG;
        it.richkmeli.jframework.util.log.Logger.error(message);
    }

    public static void error(String message, Throwable throwable) {
        debug = BuildConfig.DEBUG;
        it.richkmeli.jframework.util.log.Logger.error(message,throwable);
    }

    public static void error(Throwable throwable) {
        debug = BuildConfig.DEBUG;
        it.richkmeli.jframework.util.log.Logger.error(throwable);
    }

   /*public static void i(String message) {
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
    }*/
}
