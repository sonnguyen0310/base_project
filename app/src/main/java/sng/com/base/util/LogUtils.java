package sng.com.base.util;

import android.util.Log;

import sng.com.base.BuildConfig;

/**
 * Created by son.nguyen on 3/17/2016.
 */
public class LogUtils {

    private static boolean isDebuggable = BuildConfig.DEBUG;

    private LogUtils() {

    }

    // log verbal
    public static void v(String tag, String msg) {
        if (msg != null && isDebuggable) {
            Log.v(tag, msg);
        }
    }

    // log debug
    public static void d(String tag, String msg) {
        if (msg != null && isDebuggable) {
            Log.d(tag, msg);
        }
    }

    // log info
    public static void i(String tag, String msg) {
        if (msg != null && isDebuggable) {
            Log.i(tag, msg);
        }
    }

    // log error
    public static void e(String tag, String msg) {
        if (msg != null && isDebuggable) {
            Log.e(tag, msg);
        }
    }

    public static void printStackTrace(Exception e) {
        if (e != null && isDebuggable) {
            e.printStackTrace();
        }
    }
}
