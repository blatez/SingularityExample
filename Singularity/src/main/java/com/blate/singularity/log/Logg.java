package com.blate.singularity.log;

import android.util.Log;

import com.blate.singularity.Singularity;

public class Logg {

    private static final String TAG = "Logg";

    private static boolean sActive = Singularity.debug();
    private static boolean sLatchActive = false;

    public static void setActive(boolean active) {
        if (sLatchActive) {
            Log.w(TAG, String.format("[active] set multiple times; set value[%s]fail; the current using value [%s]",
                    active, sActive));
            return;
        }
        sActive = active;
        sLatchActive = true;
    }

    public static void i(String tag, String msg) {
        if (sActive) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (sActive) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (sActive) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sActive) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (sActive) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (sActive) {
            Log.e(tag, msg, tr);
        }
    }

}
